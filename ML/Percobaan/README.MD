# Guidance on Final_Face_Model.py

The 'Final_Face_Model.py' file is a model designed to read user faces. This file uses face recognition methods to identify faces from the given images. 
Before using this model, make sure you have two additional files: 'data.pkl' and 'haarcascade_frontalface_default.xml'. 'data.pkl' contains the face signatures from the training data, while 'haarcascade_frontalface_default.xml' is used to detect faces in images.

## Instructions
1. Prepare Supporting Files:
Make sure you have three required files: 'Final_Face_Model.py', 'data.pkl', and 'haarcascade_frontalface_default.xml'. Place these three files in the same folder.

2. Execute the Model:
Run the 'Final_Face_Model.py' file using Python. You can execute this file in the terminal or a Python development environment like Jupyter Notebook.
```python
python Final_Face_Model.py
```
3. Using the Model:
After the file is executed, the model will start the face recognition process. The model will use 'haarcascade_frontalface_default.xml' to detect faces in the given images. 
Then, the model will match the detected faces with the face signatures stored in 'data.pkl'. If a face is recognized, the model will provide output by displaying the name of the recognized user.

4. User Interaction:
Follow the instructions displayed on the screen to take a photo or image of the face. Press the appropriate button to capture the image, and wait for the face recognition result.
