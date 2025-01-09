from django.shortcuts import render, redirect
from django.contrib.auth import authenticate, login
from django.contrib.auth.forms import UserCreationForm
from django.contrib import messages
from django.contrib.auth.models import User  # <-- Import the User model
from django.conf import settings
from django.core.mail import send_mail
from .models import TeamRegistration




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
            return redirect('register_team')  # Use the URL pattern name
        else:
            messages.error(request, 'Invalid username or password.')
            return redirect('login')  # Redirect back to login page on error
        
    return render(request, 'accounts/login.html')

# Protected Home view that requires login


def register_team(request):
    if request.method == 'POST':
        # Handle form submission
        pass
    return render(request, 'accounts/register_team.html')






def register_team(request):
    if request.method == 'POST':
        # Extract form data
        team_name = request.POST['team_name']
        coach_name = request.POST['coach_name']
        coach_phone = request.POST['coach_phone']
        coach_email = request.POST['coach_email']
        manager_name = request.POST['manager_name']
        manager_phone = request.POST['manager_phone']
        manager_email = request.POST['manager_email']
        team_category = request.POST['team_category']
        num_players = request.POST['num_players']
        home_ground = request.POST['home_ground']
        training_needs = request.POST['training_ground_needs']
        equipment_needed = request.POST['equipment_needed']

        # Save data to the database
        TeamRegistration.objects.create(
            team_name=team_name,
            coach_name=coach_name,
            coach_phone=coach_phone,
            coach_email=coach_email,
            manager_name=manager_name,
            manager_phone=manager_phone,
            manager_email=manager_email,
            team_category=team_category,
            num_players=num_players,
            home_ground=home_ground,
            training_ground_needs=training_needs,
            equipment_needed=equipment_needed,
        )

        # Send email to the organization
        subject = f"New Team Registration: {team_name}"
        message = f"""
        A new team has been registered.

        Team Name: {team_name}
        Coach Name: {coach_name}
        Coach Phone: {coach_phone}
        Coach Email: {coach_email}
        Manager Name: {manager_name}
        Manager Phone: {manager_phone}
        Manager Email: {manager_email}
        Team Category: {team_category}
        Number of Players: {num_players}
        Home Ground: {home_ground}
        Training Needs: {training_needs}
        Equipment Needed: {equipment_needed}
        """
        recipient_list = ['dorothy@ligiopen.com']  # Replace with the organization's email
        send_mail(subject, message, settings.EMAIL_HOST_USER, recipient_list)

        # Redirect to a success page
        return redirect('success_page')  # Replace with your success page URL name

    return render(request, 'accounts/register_team.html')


def success_page(request):
    return render(request, 'accounts/sucess.html')