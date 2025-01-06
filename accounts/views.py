from django.shortcuts import render, redirect
from django.contrib.auth import authenticate, login
from django.contrib.auth.forms import UserCreationForm
from django.contrib.auth.decorators import login_required
from django.contrib import messages
from .forms import CustomLoginForm
from django.contrib.auth.models import User  # <-- Import the User model


# Register view
def register_view(request):
    if request.method == 'POST':
        username = request.POST.get('username')
        password = request.POST.get('password')
        confirm_password = request.POST.get('confirm_password')
        role = request.POST.get('role')

        # Validation for matching passwords
        if password != confirm_password:
            messages.error(request, 'Passwords do not match.')
            return redirect('register')

        # Create a new user
        user = User.objects.create_user(username=username, password=password)

        # Add role or other additional fields if needed
        if role == 'Coach':
            # Assign Coach role, if necessary
            pass
        elif role == 'Admin':
            # Assign Admin role, if necessary
            pass
        elif role == 'Player':
            # Assign Player role, if necessary
            pass
        
        messages.success(request, 'Account created successfully! Please log in.')
        return redirect('login')
    return render(request, 'accounts/register.html')

# Login view
def login_view(request):
    if request.method == 'POST':
        username = request.POST.get('username')
        password = request.POST.get('password')

        user = authenticate(request, username=username, password=password)
        
        if user is not None:
            login(request, user)
            return redirect('home')  # Redirect to homepage after successful login
        else:
            messages.error(request, 'Invalid username or password.')
            return redirect('login')
        
    return render(request, 'accounts/login.html')

# Protected Home view that requires login
@login_required
def home(request):
    return render(request, 'accounts/dashboard.html')  # Replace with your home template
