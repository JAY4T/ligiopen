# 📦 LigiOpen - Every Match Matters

**LigiOpen** is a grassroots football management platform built with **ReactJS** (frontend) and **Django REST Framework** (backend). It enables organizers, teams, and fans to actively engage with local football tournaments through real-time updates and data tracking.

## 🎯 Key Features


- 📅 Match fixtures and live score updates  
- ⚽ Player profiles with stats tracking  
- 📊 Live match updates and result management  
- 🌍 Public access to league data and highlights  

LigiOpen aims to empower community football by providing a centralized platform for managing league activities, enhancing transparency, and improving the visibility of local talent.

## 🔧 Technologies Used

### Frontend (ReactJS)
- React
- Axios
- React Router DOM
- Bootstrap

### Backend (Django REST Framework)
- Django
- Django REST Framework
- PostgreSQL 
- djangorestframework-cors-headers

## 🚀 Getting Started

### 🔹 Prerequisites
Make sure you have installed:
- Node.js & npm
- Python 3.x & pip
- PostgreSQL (if used)
- Virtualenv (optional but recommended)

---


### 🔹 Backend Setup (Django REST Framework)

```
# Clone the repo and navigate to backend

git clone https://github.com/JAY4T/ligiopenV2.git
cd backend

# Create virtual environment and activate

python -m venv env
source env/bin/activate

# On Windows use

`env\Scripts\activate`

# Install dependencies

pip install -r requirements.txt

# Run migrations
python manage.py migrate

# Start the server
python manage.py runserver
```

### 🔹 Frontend Setup (ReactJS)

```
# Navigate to the frontend directory
cd ../frontend

# Install dependencies
npm install

# Start the React development server
npm start
```
