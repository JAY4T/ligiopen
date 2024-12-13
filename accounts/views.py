# accounts/views.py
from django.shortcuts import render, redirect
from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.models import User
from django.contrib import messages
from .models import Profile


def register_view(request):
    if request.method == 'POST':
        username = request.POST.get('username')  # Use .get() to avoid errors if the field is missing
        password = request.POST.get('password')
        password_confirm = request.POST.get('password_confirm')  # Using .get() here as well
        user_type = request.POST.get('user_type')  # Retrieve user_type from form
        
        if not user_type:
            messages.error(request, 'Please select a user type')
            return render(request, 'accounts/register.html')

        if password != password_confirm:
            messages.error(request, 'Passwords do not match')
            return render(request, 'accounts/register.html')

        if User.objects.filter(username=username).exists():
            messages.error(request, 'Username already exists')
            return render(request, 'accounts/register.html')

        try:
            # Create the user and the corresponding profile
            user = User.objects.create_user(username=username, password=password)
            Profile.objects.create(user=user, user_type=user_type)  # Associate user_type with profile
            messages.success(request, 'Registration successful. Please log in.')
            return redirect('login')
        except Exception as e:
            messages.error(request, f'Error creating profile: {str(e)}')
            user.delete()  # Clean up the user if profile creation fails

    return render(request, 'accounts/register.html')



def login_view(request):
    if request.method == 'POST':
        username = request.POST['username']
        password = request.POST['password']
        
        user = authenticate(request, username=username, password=password)
        
        if user is not None:
            login(request, user)
            
            # Check if profile exists, create it if not
            if not hasattr(user, 'profile'):
                Profile.objects.create(user=user, user_type='Coach')  # Default to 'Coach' if profile is missing

            # Redirect based on user_type
            user_type = user.profile.user_type
            if user_type == 'Admin':
                return redirect('admin_dashboard')
            elif user_type == 'Coach':
                return redirect('coach_dashboard')
        else:
            messages.error(request, 'Invalid username or password')

    return render(request, 'accounts/login.html')


def logout_view(request):
    logout(request)
    messages.success(request, 'You have been logged out.')
    return redirect('login')


def coach_dashboard(request):
    # Your logic for coach dashboard goes here
    return render(request, 'accounts/coach_dashboard.html')


def admin_dashboard(request):
    return render(request, 'accounts/admin_dashboard.html')
