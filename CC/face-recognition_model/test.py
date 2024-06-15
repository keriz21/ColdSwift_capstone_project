import os
import pickle
from os import listdir
from os.path import join
from PIL import Image
import cv2
import numpy as np
from numpy import expand_dims
from keras_facenet import FaceNet

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

def load_database():
    script_dir = os.path.dirname(os.path.realpath(__file__))
    data_path = os.path.join(script_dir, "data.pkl")

    if not os.path.exists(data_path):
        raise FileNotFoundError(f"File not found: {data_path}")

    with open(data_path, "rb") as myfile:
        database = pickle.load(myfile)

    return database

def main():

    HaarCascade = cv2.CascadeClassifier(cv2.samples.findFile(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml'))
    MyFaceNet = FaceNet()

    # Load the database
    database = load_database()

    # Directory containing test images
    current_dir = os.path.dirname(__file__)
    test_dir = os.path.join(current_dir, 'data_test/')

    if not os.path.exists(test_dir):
        print("Error: Test directory does not exist.")
        return

    # Specific image to test
    specific_image = 'Mida Hasian.jpg'
    img_path = os.path.join(test_dir, specific_image)

    if not os.path.exists(img_path):
        print(f"Error: Image {specific_image} does not exist in the test directory.")
        return

    print(f"Processing image {specific_image}...")

    frame = cv2.imread(img_path)
    if frame is None:
        print(f"Error: Could not read image {img_path}")
        return

    processed_image = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    processed_image = Image.fromarray(processed_image)
    processed_image = processed_image.resize((160, 160))
    processed_image = np.asarray(processed_image)

    embeddings = MyFaceNet.embeddings(expand_dims(processed_image, axis=0))

    min_dist = float('inf')
    identity = None

    for key, value in database.items():
        dist = np.linalg.norm(value - embeddings)
        if dist < min_dist:
            min_dist = dist
            identity = key

    if identity:
        print(f'Predicted for {specific_image}: {identity}')
    else:
        print(f"No identity found for {specific_image}")

    print("Processing complete.")

if __name__ == "__main__":
    main()
