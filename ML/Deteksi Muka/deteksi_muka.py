import cv2

'''
    TUtorial mengambil foto untuk dataset
    siapkan folder di folder dataset
        ML/Deteksi Muka/DataSet
    copy path folder tersebut ke variabel path
    jalankan program

    tekan dan tahan tombol 'm' untuk menangkap gambar 1 per satu
    tekan tombol 'c' untuk mengankgap gambar sekaligus
'''


# Inisialisasi classifier wajah
face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')
path = "ML/Deteksi Muka/DataSet/Reza"
# Fungsi untuk mengambil gambar
def capture_images():
    cap = cv2.VideoCapture(0)  # Buka webcam
    count = 0  # Counter untuk jumlah frame yang diambil

    tombol = False
    count = 0

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
                tombol = not tombol

            if tombol:
                if count <= 100:
                    cv2.imwrite(path + f'foto_{count}.jpg', face_crop)
                    print(f'foto_{count}.jpg captured')
                    count += 1
            
            if cv2.waitKey(1) & 0xFF == ord('m'):
                if count <= 100:
                    cv2.imwrite(path + f'foto_{count}.jpg', face_crop)
                    print(f'foto_{count}.jpg captured')
                    count += 1
            

        # Keluar dari loop jika tombol 'q' ditekan
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    # Tutup webcam dan jendela OpenCV
    cap.release()
    cv2.destroyAllWindows()

# Panggil fungsi untuk memulai proses pengambilan gambar
capture_images()
