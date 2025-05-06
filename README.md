# ⚽ LigiOpen New Version

**LigiOpen** is a full-stack web app powered by **Django REST Framework** and **ReactJS**. It brings grassroots football online by offering **live scores**, **fixtures**, **team standings**, **player stats**, and **news updates**.

---

## 🌟 Features

- 🔴 Live Scores  
- 🏆 Fixtures & Results  
- 👥 Player & Team Profiles  
- 📝 Club Registration  
- 📊 Stats Table  
- 📰 News & Updates  

---

## 🚀 Tech Stack

### 🧠 Backend (API)
- Django + DRF
- PostgreSQL
- Django Admin Panel
- Postman (for testing)

### 🎨 Frontend
- ReactJS
- Axios (API calls)
- Bootstrap

---

## 🛠️ Backend Installation (Django)

### ✅ Clone the Repository

```bash
git clone https://github.com/JAY4T/ligiopenV2.git
cd ligiopenweb/backend
🧪 Create & Activate Virtual Environment

python -m venv env

# For Linux/macOS
source env/bin/activate

# For Windows
env\Scripts\activate
📦 Install Dependencies

pip install -r requirements.txt
⚙️ Configure the Database
In settings.py,  PostgreSQL:


DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.sqlite3',
        'NAME': BASE_DIR / 'db.sqlite3',
    }
}
🔄 Run Migrations & Create Superuser

python manage.py makemigrations
python manage.py migrate
python manage.py createsuperuser
🚀 Start Backend Server

python manage.py runserver
API available at: http://127.0.0.1:8000/api/

🎨 Frontend Installation (React)
📁 Navigate to Frontend Directory

cd ../frontend
📦 Install Dependencies

npm install
⚙️ Add Backend URL to .env
Create a .env file and add:


REACT_APP_API_URL=http://127.0.0.1:8000/api/
🚀 Start React Server

npm start
Frontend runs at: http://localhost:3000

🔄 Connect Frontend to Backend
Ensure both servers are running

Frontend uses Axios and the REACT_APP_API_URL to fetch data from the backend

🌐 Deployment
Use these platforms:

Backend: Heroku, Railway, Render

Frontend: Netlify, Vercel

Update Django settings.py for CORS:


INSTALLED_APPS += ['corsheaders']

MIDDLEWARE = [
    'corsheaders.middleware.CorsMiddleware',
    ...
]

CORS_ALLOW_ALL_ORIGINS = True  # or use CORS_ALLOWED_ORIGINS for specific domains
🗂️ Project Structure

ligiopenweb/
├── backend/          # Django backend
│   ├── manage.py
│   ├── ligiopen/     # Main Django project
│   └── api/          # Core app with models, views, etc.
│
├── frontend/         # React frontend
│   ├── package.json
│   ├── src/
│   └── public/
🤝 Contributing
Pull requests are welcome!
Fork the repo → Create a branch → Make changes → Submit PR ✅

🪪 License
MIT License

