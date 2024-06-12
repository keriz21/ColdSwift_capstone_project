import os
import pickle
from PIL import Image
from keras_facenet import FaceNet
import numpy as np
from numpy import expand_dims
import cv2

def load_database():
    # Construct the absolute path to data.pkl
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

    # Initialize the video capture
    cap = cv2.VideoCapture(0)

    if not cap.isOpened():
        print("Error: Could not open webcam.")
        return

    print("Tekan 's' untuk mengambil foto, dan 'q' untuk keluar.")

    while True:
        # Read frame 
        ret, frame = cap.read()
        
        if not ret:
            print("Error: Failed to capture image")
            break
        
        # Shows frame
        cv2.imshow('Webcam', frame)
        
        # Press 's' to take a picture
        key = cv2.waitKey(1) & 0xFF
        if key == ord('s'):
            # Process the picture
            processed_image = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)  # Convert picture's color from BGR to RGB
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
            
            # Shows the prediction output
            print(f'Predicted: {identity}')
            cv2.putText(frame, f'Predicted: {identity}', (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 0, 0), 2)
            cv2.imshow('Webcam', frame)
            
        # Press 'q' to quit
        if key == ord('q'):
            break

    # Close the window and frame
    cap.release()
    cv2.destroyAllWindows()

if __name__ == "__main__":
    main()
