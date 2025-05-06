⚽ LigiOpen Version2
LigiOpen is a full-stack web app built with Django REST Framework and ReactJS for managing live scores, fixtures, player stats, and news.

🌟 Features
🔴 Live Scores

🏆 Fixtures & Results

👥 Player & Team Profiles

📊 Stats Table

🚀 Tech Stack
Backend: Django, DRF, PostgreSQL

Frontend: ReactJS, Axios, Bootstrap

🛠️ Setup
Backend (Django)
bash
Copy
Edit
git clone https://github.com/JAY4T/ligiopenV2.git
cd ligiopenweb/backend

# Create virtual environment
python -m venv env
source env/bin/activate  # macOS/Linux
env\Scripts\activate     # Windows

# Install dependencies
pip install -r requirements.txt

# Configure PostgreSQL in settings.py, then run:
python manage.py makemigrations
python manage.py migrate
python manage.py createsuperuser
python manage.py runserver
API: http://127.0.0.1:8000/api/

Frontend (React)
bash
Copy
Edit
cd ../frontend
npm install
npm start
App: http://localhost:3000

🌐 Deployment
Backend: Use ngrok to expose the API

Frontend: Deploy on Netlify or Vercel

Make sure to enable CORS:

python
Copy
Edit
INSTALLED_APPS += ['corsheaders']
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
Fork → Create a new branch → Make changes → Submit a Pull Request

📌 Conventional Commit Types
Type	Purpose
feat	Add a new feature
fix	Fix a bug or issue
docs	Update documentation
refactor	Improve code (no behavior change)
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
