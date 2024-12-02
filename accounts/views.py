
from django.shortcuts import render, redirect
from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.models import User
from django.contrib import messages
from .models import Profile

from .forms import TeamRegistrationForm  


def register_view(request):
    if request.method == 'POST':
        username = request.POST.get('username')
        password = request.POST.get('password')
        password_confirm = request.POST.get('password_confirm')
        user_type = request.POST.get('user_type')  # Retrieve user_type from form

        if not user_type:
            messages.error(request, 'Please select a user type')
            return render(request, 'accounts/register.html')

        if password == password_confirm:
            if User.objects.filter(username=username).exists():
                messages.error(request, 'Username already exists')
            else:
                try:
                    user = User.objects.create_user(username=username, password=password)
                    Profile.objects.create(user=user, user_type=user_type)
                    messages.success(request, 'Registration successful. Please log in.')
                    return redirect('login')
                except Exception as e:
                    messages.error(request, f'Error creating user: {str(e)}')
        else:
            messages.error(request, 'Passwords do not match')

    return render(request, 'accounts/register.html')


def login_view(request):
    if request.method == 'POST':
        username = request.POST.get('username')
        password = request.POST.get('password')

        user = authenticate(request, username=username, password=password)
        if user is not None:
            login(request, user)
            if hasattr(user, 'profile'):  # Ensure the user has a profile
                user_type = user.profile.user_type
                if user_type == 'coach':
                    return redirect('coach_dashboard')  # Create this URL
                elif user_type == 'admin':
                    return redirect('admin_dashboard')  # Create this URL
                elif user_type == 'player':
                    return redirect('player_dashboard')
            else:
                messages.error(request, 'User profile is incomplete. Contact support.')
        else:
            messages.error(request, 'Invalid username or password')

    return render(request, 'accounts/login.html')


def logout_view(request):
    logout(request)
    messages.success(request, 'You have been logged out.')
    return redirect('login')


def player_dashboard(request):
    return render(request, 'accounts/player_dashboard.html')


def coach_dashboard(request):
    return render(request, 'accounts/coach_dashboard.html')  # Template for coach


def admin_dashboard(request):
    return render(request, 'accounts/admin_dashboard.html')  # Template for admin


def team_register(request):
    return render(request, 'team_register.html')

def dashboard_view(request):
    return render(request, 'dashboard.html') 

def register_team(request):
    if request.method == 'POST':
        form = TeamRegistrationForm(request.POST)
        if form.is_valid():
            form.save()  # Save the new team to the database
            return redirect('team_list')  # Redirect to team list page after saving
    else:
        form = TeamRegistrationForm()

    return render(request, 'register_team.html', {'form': form})