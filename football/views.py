from django.shortcuts import render, redirect, get_object_or_404
from .models import Staff, Player, Club, FixtureResult, Fixture
from django.contrib.auth import authenticate, login
from django.http import HttpResponse
from django.core.mail import send_mail 
from django.conf import settings
from .forms import ContactForm
from django.contrib import messages
from django.urls import reverse
from .models import Matchday
from .models import Team, Fixture

# from .models import Blog
# from .models import BlogPost
from .models import Club
import qrcode
import io
import base64


def home(request):
    clubs = Club.objects.all()
    matchdays = Matchday.objects.all()
    staffs = Staff.objects.all()  # Fetch all staff members
    fixtures = Fixture.objects.all()


    return render(request, 'index.html', {
        'clubs': clubs,
        'matchdays': matchdays,
        'staffs': staffs,
        'fixtures': fixtures

    })


 

def fixtures_view(request):
    fixtures_results = FixtureResult.objects.all() 
    fixtures = Fixture.objects.all()

    
    return render(request, 'fixtures.html', {
        'fixtures_results': fixtures_results, 'fixtures':fixtures
    })

def club_staff_view(request, club_name):
    club = get_object_or_404(Club, name=club_name)  # or use 'slug' if you're using slugs
    coaching_staff = Staff.objects.filter(club=club, staff_type='Coaching Staff')
    other_staff = Staff.objects.filter(club=club, staff_type='Other Staff')
    club_match_staff = Staff.objects.filter(club=club, staff_type='Club Match Staff')
    players = Player.objects.filter(club=club)

    context = {
        'club': club,
        'coaching_staff': coaching_staff,
        'other_staff': other_staff,
        'club_match_staff': club_match_staff,
        'players': players,
    }
    return render(request, 'pages/club-staff.html', context)


def player_stats_view(request):
    stats = Stat.objects.all()
    players = Player.objects.all()

    return render(request, 'pages/club-staff.html', {'stats': stats, 'players': players})






def contact_us_view(request):
    if request.method == 'POST':
        form = ContactForm(request.POST)
        if form.is_valid():
            name = form.cleaned_data['name']
            email = form.cleaned_data['email']  # Sender's email
            issue = form.cleaned_data['issue']  # Message from the form

            try:
                # 1. Send email to admin (info@ligiopen.com)
                send_mail(
                    subject=f"Message from {name}",
                    message=f"Issue: {issue}\n\nFrom: {name}\nEmail: {email}",
                    from_email='info@ligiopen.com',
                    recipient_list=['info@ligiopen.com'],
                    fail_silently=False,
                )

                # 2. Send confirmation email to the sender
                send_mail(
                    subject="Thank you for contacting us",
                    message=f"Hi {name},\n\nThank you for reaching out! We have received your message:\n\n{issue}\n\nWe will get back to you shortly.\n\nBest regards,\nLigi Open Team",
                    from_email='info@ligiopen.com',
                    recipient_list=[email],  # Send to the sender's email
                    fail_silently=False,
                )

                # Success message and redirection
                messages.success(request, 'Your message has been sent successfully!')
                success_url = reverse('success') + f'?email={email}'
                return redirect(success_url)
            except Exception as e:
                messages.error(request, f'An error occurred while sending your message: {str(e)}')
                return redirect('contact_us')
    else:
        form = ContactForm()

    return render(request, 'contact.html', {'form': form})

def success_view(request):
    email = request.GET.get('email', '')
    return render(request, 'pages/success.html', {'email': email})



def results_view(request):
    results = Result.objects.all()
    return render(request, 'pages/club-staff.html', {'results': results})


def club_list_view(request):
    clubs = Club.objects.all()
    return render(request, 'index.html', {'clubs': clubs})


def club_detail_view(request, club_id):
    club = get_object_or_404(Club, id=club_id)
    players = club.players.all()
    fixtures = club.fixtures.all()
    results = Result.objects.filter(fixture__club=club)

     
    
    return render(request, 'pages/club-staff.html', {'club': club, 'players': players,'fixtures':fixtures,'results':results})



def all_players_view(request):
    players = Player.objects.all()  # Fetch all players from the database
    return render(request, 'pages/all-players.html', {'players': players})

def player_list(request):
    players = Player.objects.all()
    return render(request, 'pages/all-players.html', {'players': players})



def blog_view(request):
    blogs = Blog.objects.all()
    return render(request, 'index.html', {'blogs': blogs})


def blog_list(request):
    blog_posts = BlogPost.objects.all()
    return render(request, 'pages/blog.html', {'blog_posts': blog_posts})



def highlight_list(request):
    highlights = Highlight.objects.all()
    return render(request, 'index.html', {'highlights': highlights})


def matchday_highlights(request):
    fixtures = Fixture.objects.all()
    for fixture in fixtures:
        fixture.home_goals = fixture.goals.filter(team='home')
        fixture.away_goals = fixture.goals.filter(team='away')
    return render(request, 'pages/match-report.html', {'fixtures': fixtures})



def match_report(request):
    fixtures = Fixture.objects.all()
    return render(request, 'pages/match-report.html', {'fixtures': fixtures})



def feedback_view(request):
    if request.method == 'POST':
        form = FeedbackForm(request.POST)
        if form.is_valid():
            form.save()  # Save the feedback in the database
            return redirect('feedback_success')  # Redirect to success page after submission
    else:
        form = FeedbackForm()

    return render(request, 'feedback/feedback.html', {'form': form})


def team_fixtures(request, team_id):
    team = get_object_or_404(Team, id=team_id)  # Fetch the team object
    fixtures = Fixture.objects.filter(opponent=team).order_by('date')
    context = {
        'fixtures': fixtures,
        'team': team,  # Pass the team object to the context
    }
    return render(request, 'pages/club-staff.html', context)

def team_results(request, team_id):
    team = get_object_or_404(Team, id=team_id)  # Fetch the team object
    fixtures_with_results = Fixture.objects.filter(opponent=team).select_related('result').order_by('date')
    context = {
        'fixtures_with_results': fixtures_with_results,
        'team': team,  # Pass the team object to the context
    }
    return render(request, 'pages/club-staff.html', context)


def feedback_success_view(request):
    return render(request, 'feedback/feedback_success.html')

#FAQ Section views
def faq_page(request):
    return render(request, 'pages/FAQ/faq.html')

def website_page(request):
    return render(request, 'pages/FAQ/website.html')

def about_us_page(request):
    return render(request, 'pages/FAQ/about-us.html')

def financial_page(request):
    return render(request, 'pages/FAQ/financial.html')

def tickets_page(request):
    return render(request, 'pages/FAQ/tickets.html')

def support_page(request):
    return render(request, 'pages/FAQ/support.html')

def data_privacy_page(request):
    return render(request, 'pages/FAQ/data-privacy.html')

def clubs_page(request):
    return render(request, 'pages/FAQ/clubs.html')
    
    #Open Finance views
def finance_about(request):
    return render(request, 'pages/finance/about.html')

def finance_payment_type(request):
    return render(request, 'pages/finance/payment-type.html')

def finance_sponsor(request):
    return render(request, 'pages/finance/sponsor.html')

def crowdfund_pay(request):
    return render(request, 'pages/finance/crowdfund-pay.html')

def mpesa_donate(request):
    return render(request, 'pages/finance/mpesa-donate.html')

def other_donate(request):
    return render(request, 'pages/finance/donate.html')

def crowdfund_mpesa(request):
    return render(request, 'pages/finance/mpesa-pay.html')

def crowdfund_other(request):
    return render(request, 'pages/finance/crowdfund-card.html')


def all_players(request):
    return render(request, 'pages/all-players.html')

def matchday_highlights(request):
    return render(request, 'pages/match-report.html')

def blog(request):
    return render(request, 'pages/blog.html')

def tickets(request):
    return render(request, 'pages/tickets/ticket.html')

def tickets_pay(request):
    return render(request, 'pages/tickets/ticketpay.html')

def tickets_mpesa(request):
    team_a = request.GET.get('team_a')
    team_b = request.GET.get('team_b')
    date = request.GET.get('date')
    time = request.GET.get('time')
    venue = request.GET.get('venue')

    context = {
        'team_a': team_a,
        'team_b': team_b,
        'date': date,
        'time': time,
        'venue': venue
    }

    return render(request, 'pages/tickets/ticket-mpesa.html', context)

def tickets_card(request):
    team_a = request.GET.get('team_a')
    team_b = request.GET.get('team_b')
    date = request.GET.get('date')
    time = request.GET.get('time')
    venue = request.GET.get('venue')

    context = {
        'team_a': team_a,
        'team_b': team_b,
        'date': date,
        'time': time,
        'venue': venue
    }

    return render(request, 'pages/tickets/ticket-card.html', context)

def generate_unique_id():
    import uuid
    return str(uuid.uuid4())

def ticket_qr(request):
    if request.method == 'POST':
        first_name = request.POST.get('first_name')
        last_name = request.POST.get('last_name')
        email = request.POST.get('email')
        card_number = request.POST.get('cardNumber')
        expiry_month = request.POST.get('expiryMonth')
        cvc = request.POST.get('cvc')

        team_a = request.POST.get('team_a')
        team_b = request.POST.get('team_b')
        date = request.POST.get('date')
        time = request.POST.get('time')
        venue = request.POST.get('venue')

        unique_id = generate_unique_id()
        qr_data = (
                f"First Name: {first_name}\n"
                f"Last Name: {last_name}\n"
                f"Email: {email}\n"
                f"ID: {unique_id}\n"
                f"Event: {team_a} vs {team_b}\n"
                f"Date: {date}\n"
                f"Time: {time}\n"
                f"Venue: {venue}"
        )

        # Generate QR code
        qr = qrcode.QRCode(version=1, box_size=10, border=5)
        qr.add_data(qr_data)
        qr.make(fit=True)
        img = qr.make_image(fill='black', back_color='white')
        buffer = io.BytesIO()
        img.save(buffer, 'PNG')
        qr_code_image = buffer.getvalue()
        qr_code_base64 = base64.b64encode(qr_code_image).decode('utf-8')

        context = {
                'first_name': first_name,
                'last_name': last_name,
                'email': email,
                'unique_id': unique_id,
                'qr_code_base64': qr_code_base64,
                'team_a': team_a,
                'team_b': team_b,
                'date': date,
                'time': time,
                'venue': venue
            }
        
        return render(request, 'pages/tickets/ticket-qr.html', context)
    return HttpResponse("Invalid request")

#Navbar navigation views
def finance(request):
    return render(request, 'finance.html')

def fixtures(request):
    fixtures = Fixture.objects.all()
    matchdays = Matchday.objects.all()
    return render(request, 'fixtures.html', {'fixtures': fixtures,'matchdays': matchdays})

def blogs(request):
    blogs = Blog.objects.all()
    # highlights=Highlight.objects.all()
    return render(request, 'blog.html', {'blogs':blogs})

def contact(request):
    return render(request, 'contact.html')

def clubs(request):
    clubs = Club.objects.all()
    return render(request, 'clubs.html', {'clubs': clubs})

def feedback(request):
    return render(request, 'pages/feedback/feedback.html')

def ligiopen(request):
    staffs = Staff.objects.all()  # Fetch all staff members

    return render(request, 'pages/ligiopen.html', {'staffs':staffs})