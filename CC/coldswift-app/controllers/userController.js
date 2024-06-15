// controllers/userController.js
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const User = require('../models/User');
const { spawnSync } = require('child_process');
const fs = require('fs');
const path = require('path'); // Import the path module

function extract_ktp_info(imagePath) {
  const result = spawnSync('python', ['./middleware/ocr_model/model_ktp_ocr.py', imagePath]);

    // Log the result of the Python script execution
    console.log('Python Script Result:', result);
  if (result.error) {
    throw result.error;
  }
  return result.stdout.toString();
}

function extract_face_info(imagePath) {
  console.log('Calling Python script for face recognition with image path:', imagePath);
  const result = spawnSync('python', ['./middleware/face-recognition_model/Final_Face_Model.py', imagePath]);

    // Log the result of the Python script execution
    console.log('Python Script Result:', result);
  if (result.error) {
    throw result.error;
  }

  if (result.stderr) {
    console.error('Python script stderr:', result.stderr);
  }
  console.log('Python script stdout:', result.stdout);
  console.log(result.stdout.toString);
  return result.stdout.toString();
}

exports.register = async (req, res) => {

  // check the req data
  console.log('Body:', req.body);
  console.log('File:', req.files);
  const { email, password } = req.body;
    
  const ktpImage = req.files['ktp_image'] ? req.files['ktp_image'][0] : null;
  const faceImage = req.files['face_image'] ? req.files['face_image'][0] : null;
    
  if (!ktpImage || !faceImage) {
      return res.status(400).send('Both images are required.');
  }

  // Generate a unique temporary file path
  const uploadsDir = path.join(__dirname, '../temp_data');
  const ktpDir = path.join(uploadsDir, '/ktp-images');
  const ktp_tempPath = path.join(ktpDir, `${Date.now()}.png`);
  const faceDir = path.join(uploadsDir, '/face-images');
  const face_tempPath = path.join(faceDir, `${Date.now()}.png`);

  try {
    // Ensure the uploads directory exists
    if (!fs.existsSync(ktpDir)) {
      fs.mkdirSync(ktpDir);
    }else if (!fs.existsSync(faceDir)) {
      fs.mkdirSync(faceDir);
    } 

    // Write the file buffer to a temporary file
    fs.writeFileSync(ktp_tempPath, ktpImage.buffer);
    fs.writeFileSync(face_tempPath, faceImage.buffer);

    // Log the temporary file path before using it
    console.log('Temporary File Path:', ktp_tempPath);
    console.log('Temporary File Path:', face_tempPath);

    const ocrResult = extract_ktp_info(ktp_tempPath);
    //Log ocr model result
    console.log('OCR Result:', ocrResult);
    const { nik, name } = JSON.parse(ocrResult);
    
    if (!nik || !name) {
      return res.status(400).json({ message: 'Failed to extract NIK or Name from the image, please take photos steadily ' });
    }

    const faceResult = extract_face_info(face_tempPath);
    //Log face model result
    console.log('Face Result:', faceResult);
    // const { name_FR } = JSON.parse(faceResult);
    
    if (!name_FR) {
      return res.status(400).json({ message: 'Failed to recognize user, please take photos steadily' });
    }

    if (name != name_FR) {
      return res.status(400).json({ message: 'the face is not the same as the ID photo' });
    }

    // Clean up the temporary file
    // fs.unlinkSync(ktp_tempPath);
    // fs.unlinkSync(face_tempPath);
    
    
    let fixedName = ''
    if (name.startsWith('Nama ')) {
      fixedName = name.replace(/^Nama\s/, ''); // Remove the 'Nama ' prefix
    }

    // Check if NIK already exists
    const existingUser = await User.findOne({ where: { nik } });
    if (existingUser) {
      return res.status(400).json({ message: 'NIK already registered' });
    }
    
    // Hash password
    const hashedPassword = await bcrypt.hash(password, 10);

    // check the data
    console.log(nik, name, fixedName, email, hashedPassword);
    
    // Create new user
    const newUser = await User.create({ nik, name: fixedName, email, password: hashedPassword });
    res.status(201).json(newUser);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};

exports.login = async (req, res) => {
  const { nik, email, password } = req.body;

  try {
    const user = await User.findOne({ where: { nik, email } });
    if (!user) {
      return res.status(400).json({ message: 'Invalid credentials' });
    }

    const isMatch = await bcrypt.compare(password, user.password);
    if (!isMatch) {
      return res.status(400).json({ message: 'Invalid credentials' });
    }

    const token = jwt.sign({ id: user.id }, process.env.JWT_SECRET, { expiresIn: '1h' });
    res.status(200).json({ token });
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};

exports.forgetPassword = async (req, res) => {
  const { nik, email, newPassword } = req.body;

  try {
    const user = await User.findOne({ where: { nik, email } });
    if (!user) {
      return res.status(400).json({ message: 'User not found' });
    }

    const hashedPassword = await bcrypt.hash(newPassword, 10);
    user.password = hashedPassword;
    await user.save();

    res.status(200).json({ message: 'Password updated successfully' });
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};

exports.viewProfile = async (req, res) => {
  const { id } = req.user;

  try {
    const user = await User.findByPk(id, { attributes: ['nik', 'name', 'email'] });
    if (!user) {
      return res.status(404).json({ message: 'User not found' });
    }

    res.status(200).json(user);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};
