import cv2

# Inisialisasi classifier wajah
face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')

# Fungsi untuk mengambil gambar
def capture_images():
    cap = cv2.VideoCapture(0)  # Buka webcam
    count = 0  # Counter untuk jumlah frame yang diambil

    while True:
        ret, frame = cap.read()  # Baca frame dari webcam
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)  # Konversi ke grayscale
        
        # Deteksi wajah
        faces = face_cascade.detectMultiScale(gray, 1.3, 5)
        
        # Jika wajah terdeteksi
        if len(faces) > 0:
            # Ambil koordinat wajah pertama
            x, y, w, h = faces[0]
            
            # Gambar kotak di sekitar wajah
            cv2.rectangle(frame, (x, y), (x+w, y+h), (255, 0, 0), 2)
            
            # Crop bagian wajah
            face_crop = frame[y:y+h, x:x+w]
            
            # Menampilkan frame dengan wajah terdeteksi
            cv2.imshow('Face Detection', face_crop)
            
            # Simpan gambar jika tombol 'c' ditekan
            if cv2.waitKey(1) & 0xFF == ord('c'):
                count += 1
                cv2.imwrite(f'captured_{count}.jpg', face_crop)
                print(f"Image {count} captured!")
                
                # Ambil 100 frame
                if count == 100:
                    break

        # Keluar dari loop jika tombol 'q' ditekan
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    # Tutup webcam dan jendela OpenCV
    cap.release()
    cv2.destroyAllWindows()

# Panggil fungsi untuk memulai proses pengambilan gambar
capture_images()
