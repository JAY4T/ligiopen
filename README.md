# ⚽ LigiOpen New Version

**LigiOpen** is a full-stack web application built with **Django REST Framework** and **ReactJS**. It provides grassroots football communities with a platform for **live scores**, **fixtures**, **team standings**, **player statistics**, and **news updates**.

---

## 🌟 Features

- 🔴 **Live Scores**: Real-time updates for ongoing matches  
- 🏆 **Fixtures & Results**: View and track match schedules and outcomes  
- 👥 **Player & Team Profiles**: Detailed profiles for players and teams  
- 📝 **Club Registration**: Register clubs with all necessary details  
- 📊 **Stats Table**: Team standings and player statistics  
- 📰 **News & Updates**: Latest football news and updates  

---

## 🚀 Tech Stack

### 🧠 Backend (API)
- **Django** + **Django REST Framework (DRF)**  
- **PostgreSQL** (Database)
- **Django Admin Panel** (for easy management)
- **Postman** (for API testing)

### 🎨 Frontend
- **ReactJS**
- **Axios** (for API communication)
- **Bootstrap** (for styling)

---

## 🛠️ Backend Installation (Django)

### ✅ Clone the Repository

First, clone the repository and navigate to the backend folder:

```bash
git clone https://github.com/JAY4T/ligiopenV2.git
cd ligiopenweb/backend
🧪 Create & Activate Virtual Environment
Create and activate the virtual environment to install dependencies:

bash
Copy
Edit
python -m venv env

# For Linux/macOS
source env/bin/activate

# For Windows
env\Scripts\activate
📦 Install Dependencies
Install all the required dependencies from requirements.txt:

bash
Copy
Edit
pip install -r requirements.txt
⚙️ Configure the Database
In the settings.py file, configure PostgreSQL as your database:

python
Copy
Edit
DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.PostgreSQL',
        'NAME': BASE_DIR / 'db.PostgreSQL',
    }
}
🔄 Run Migrations & Create Superuser
Run migrations to set up the database schema, and create a superuser to access the Django admin panel:

bash
Copy
Edit
python manage.py makemigrations
python manage.py migrate
python manage.py createsuperuser
🚀 Start the Backend Server
Start the Django development server to run the backend:

bash
Copy
Edit
python manage.py runserver
The API will be available at: http://127.0.0.1:8000/api/

🎨 Frontend Installation (React)
📁 Navigate to the Frontend Directory
Move to the frontend directory:

bash
Copy
Edit
cd ../frontend
📦 Install Dependencies
Install the required dependencies for the frontend:

bash
Copy
Edit
npm install
⚙️ Add Backend URL to .env
Create a .env file and add the backend API URL:

ini
Copy
Edit
REACT_APP_API_URL=http://127.0.0.1:8000/api/
🚀 Start the React Server
Start the React development server:

bash
Copy
Edit
npm start
The frontend will be available at: http://localhost:3000

🔄 Connect Frontend to Backend
Ensure both servers are running:

Backend: http://127.0.0.1:8000

Frontend: http://localhost:3000

The frontend uses Axios and the REACT_APP_API_URL from .env to communicate with the Django backend.

🌐 Deployment
Backend Deployment
For backend deployment, you can use services like ngrok (free trial) to expose your API:

ngrok: Expose the local API to the internet for frontend developers

Frontend Deployment
You can deploy the frontend using platforms like:

Netlify

Vercel

Make sure CORS is configured correctly in settings.py to allow cross-origin requests from the frontend:

python
Copy
Edit
INSTALLED_APPS += ['corsheaders']

MIDDLEWARE = [
    'corsheaders.middleware.CorsMiddleware',
    ...
]

CORS_ALLOW_ALL_ORIGINS = True  # Or specify CORS_ALLOWED_ORIGINS for security
🗂️ Project Structure
php
Copy
Edit
ligiopenweb/
├── backend/          # Django backend
│   ├── manage.py
│   ├── ligiopen/     # Main Django project folder
│   └── api/          # Core app with models, views, etc.
│
├── frontend/         # React frontend
│   ├── package.json
│   ├── src/
│   └── public/
🤝 Contributing
We welcome contributions!
To contribute, follow these steps:

Fork the repository

Create a new branch

Make your changes

Submit a pull request

🪪 License
This project is licensed under the MIT License.
