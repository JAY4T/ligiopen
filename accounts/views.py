from django.shortcuts import render


# Create your views here.
# accounts/views.py
from django.shortcuts import render, redirect
from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.models import User
from django.contrib import messages
from .models import Profile


def register_view(request):
    if request.method == 'POST':
        username = request.POST['username']
        password = request.POST['password']
        password_confirm = request.POST['password_confirm']
        user_type = request.POST.get('user_type')  # Retrieve user_type from form
        
        if not user_type:  # Check if user_type is provided
            messages.error(request, 'Please select a user type')
            return render(request, 'accounts/register.html')
        
        if password == password_confirm:
            if User.objects.filter(username=username).exists():
                messages.error(request, 'Username already exists')
            else:
                try:
                    # Create user and profile with the provided user_type
                    user = User.objects.create_user(username=username, password=password)
                    Profile.objects.create(user=user, user_type=user_type)  # Pass user_type to Profile creation
                    messages.success(request, 'Registration successful. Please log in.')
                    return redirect('login')
                except Exception as e:
                    messages.error(request, f'Error creating profile: {str(e)}')
                    user.delete()  # Clean up the user if profile creation fails
        else:
            messages.error(request, 'Passwords do not match')
    
    return render(request, 'accounts/register.html')

def login_view(request):
    if request.method == 'POST':
        username = request.POST['username']
        password = request.POST['password']
        
        user = authenticate(request, username=username, password=password)
        
        if user is not None:
            login(request, user)
            # Check if Profile exists, and create if not
            if not hasattr(user, 'profile'):
                Profile.objects.create(user=user, user_type='fan')  # Default to 'fan' if needed
                
            user_type = user.profile.user_type  # Access the user type from the profile
            
            if user_type == 'club':
                return redirect('club_dashboard')
            elif user_type == 'fan':
                return redirect('fan_dashboard')
            elif user_type == 'player':
                return redirect('player_dashboard')
        else:
            messages.error(request, 'Invalid username or password')
    
    return render(request, 'accounts/login.html')


def logout_view(request):
    logout(request)
    messages.success(request, 'You have been logged out.')
    return redirect('login')





def player_dashboard(request):
    # Your logic for player dashboard goes here
    return render(request, 'accounts/player_dashboard.html')
