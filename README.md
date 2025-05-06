# ⚽ LigiOpen Version2

LigiOpen is a full-stack web app built with Django REST Framework and ReactJS for managing live scores, fixtures, player stats, and news.

---

## 🌟 Features

- Live Scores  
- Fixtures & Results  
- Player & Team Profiles  
- Stats Table  

---

## 🚀 Tech Stack

**Backend**: Django, DRF, PostgreSQL  
**Frontend**: ReactJS, Axios, Bootstrap  

---

## 🛠️ Setup

### Backend (Django)

**Clone the repository:**
```bash
git clone https://github.com/JAY4T/ligiopenV2.git
cd ligiopenweb/backend
Create a virtual environment:

bash
Copy
Edit
python -m venv env
Activate the environment:

macOS/Linux:

bash
Copy
Edit
source env/bin/activate
Windows:

c
Copy
Edit
env\Scripts\activate
Install dependencies:

bash
Copy
Edit
pip install -r requirements.txt
Configure PostgreSQL in settings.py, then run:

bash
Copy
Edit
python manage.py makemigrations
python manage.py migrate
python manage.py createsuperuser
Start the backend server:

bash
Copy
Edit
python manage.py runserver
API available at: http://127.0.0.1:8000/api/

Frontend (React)
Navigate to the frontend directory:

bash
Copy
Edit
cd ../frontend
Install dependencies:

bash
Copy
Edit
npm install
Start the frontend server:

bash
Copy
Edit
npm start
App available at: http://localhost:3000

🌐 Deployment
Backend: Use ngrok to expose the API

Frontend: Deploy on Netlify or Vercel

Enable CORS in settings.py:

Add 'corsheaders' to INSTALLED_APPS and set:

python
Copy
Edit
CORS_ALLOW_ALL_ORIGINS = True
🗂️ Project Structure
bash
Copy
Edit
ligiopenweb/
├── backend/          # Django backend
│   ├── manage.py
│   └── api/
└── frontend/         # React frontend
    ├── package.json
    └── src/
⚠️ Note: Frontend code is still under development.

🤝 Contributing
Fork the repo

Create a new branch

Make changes

Submit a Pull Request

📌 Conventional Commit Types
Type	Purpose
feat	Add a new feature
fix	Fix a bug or issue
docs	Update documentation
refactor	Improve code without changing behavior
test	Add or update tests
chore	Routine tasks (configs, dependencies)
ci	CI setup or pipeline changes
build	Build tools or packaging changes

💡 Why Use Them?
📄 Auto-generate changelogs

🤝 Easier collaboration

🚀 CI/CD triggers and automation

🪪 License
MIT License © 2025 LigiOpen Team
