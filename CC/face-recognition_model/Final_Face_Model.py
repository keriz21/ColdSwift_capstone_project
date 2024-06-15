import os
import pickle
from os import listdir
from os.path import join
from PIL import Image
import cv2
import numpy as np
from numpy import expand_dims
from keras_facenet import FaceNet
import json

class faceInformation(object):
    def __init__(self):
        self.name = ""
class faceRecognition(object):
    def __init__(self, image_path):
        self.image_path = image_path
        self.result = faceInformation()
        self.process()

    def create_database(folder='data/'):
        database = {}
        HaarCascade = cv2.CascadeClassifier(cv2.samples.findFile(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml'))
        MyFaceNet = FaceNet()

        for filename in listdir(folder):
            path = join(folder, filename)
            gbr1 = cv2.imread(path)
            wajah = HaarCascade.detectMultiScale(gbr1, 1.1, 4)

            if len(wajah) > 0:
                x1, y1, width, height = wajah[0]
            else:
                x1, y1, width, height = 1, 1, 10, 10

            x1, y1 = abs(x1), abs(y1)
            x2, y2 = x1 + width, y1 + height

            gbr = cv2.cvtColor(gbr1, cv2.COLOR_BGR2RGB)
            gbr = Image.fromarray(gbr)
            gbr_array = np.asarray(gbr)

            face = gbr_array[y1:y2, x1:x2]

            face = Image.fromarray(face)
            face = face.resize((160, 160))
            face = np.asarray(face)

            face = expand_dims(face, axis=0)
            signature = MyFaceNet.embeddings(face)

            database[os.path.splitext(filename)[0]] = signature

        with open("data.pkl", "wb") as myfile:
            pickle.dump(database, myfile)

        print("Database created successfully.")

    def load_database(self):
        # Construct the absolute path to data.pkl
        script_dir = os.path.dirname(os.path.realpath(__file__))
        data_path = os.path.join(script_dir, "data.pkl")

        if not os.path.exists(data_path):
            raise FileNotFoundError(f"File not found: {data_path}")

        with open(data_path, "rb") as myfile:
            database = pickle.load(myfile)
        
        return database

    def process(self):
        HaarCascade = cv2.CascadeClassifier(cv2.samples.findFile(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml'))
        MyFaceNet = FaceNet()

        # Load the database
        database = self.load_database()

        image = cv2.imread(self.image_path)

        # Process the picture
        processed_image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)     # Convert picture's color from BGR to RGB
        processed_image = Image.fromarray(processed_image)       # Convert array NumPy to PIL object
        processed_image = processed_image.resize((160, 160))     # Resize picture
        processed_image = np.asarray(processed_image)            # Convert back to NumPy array
        
        # Get the picture embedding
        embeddings = MyFaceNet.embeddings(expand_dims(processed_image, axis=0))
        
        # Calculate the closest distance between picture's embedding and database embeddings
        min_dist = float('inf')
        identity = None
        for key, value in database.items():
            dist = np.linalg.norm(value - embeddings)
            if dist < min_dist:
                min_dist = dist
                identity = key
        
        # Return the prediction result as JSON
        print(identity)
        self.result.name = identity

    def to_json(self):
        return json.dumps({ "name_FR": self.result.name})
    
# ekstrak info
def extract_face_info(image_path):
    FR = faceRecognition(image_path)
    return FR.to_json()

if __name__ == "__main__":
    import sys

    if len(sys.argv) < 2:
        print("Usage: python script_name.py <image_path>")
        sys.exit(1)

    image_path = sys.argv[1]
    # print(extract_face_info(image_path))
    try:
        result = extract_face_info(image_path)
        print(result)
    except Exception as e:
        print(f"Error: {e}", file=sys.stderr)
        sys.exit(1)

# if __name__ == "__main__":
#     current_dir = os.path.dirname(__file__)
#     FILE_PATH = os.path.join(current_dir, 'fr_data-test/')
#     filePath = os.path.join(FILE_PATH, 'Rouf_2.jpg')
#     images = faceRecognition(filePath)
#     print(images.to_json())