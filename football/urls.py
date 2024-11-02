from django.urls import path
from . import views
from .views import player_stats_view
# from .views import fixtures_view 
from .views import contact_us_view, success_view
from .views import match_report

urlpatterns = [
    path('', views.home, name='home'),

    path('fixtures/', views.fixtures_view, name='fixtures'),
    path('pages/club-staff.html', views.club_staff_view, name='club_staff_page'),
    path('player-stats', views.player_stats_view, name='player_stats'),
    path('contact/', contact_us_view, name='contact_us'),
    path('pages/success.html', views.success_view, name='success'),

    path('club', views.club_list_view, name='club_list'),
    path('club/<int:club_name>/', views.club_detail_view, name='club_detail'),
    path('clubs/<str:club_name>/staff/', views.club_staff_view, name='club_staff_view'),

    path('pages/all-players', views.player_list, name='player_list'),
    path('pages/blog', views.blog_list, name='blog_list'),
    path('highlights/', views.highlight_list, name='highlight_list'),

    path('pages/matchday-highlights', match_report, name='match_report'),
    path('match-report/<int:match_id>/', match_report, name='match_report'),
    path('feedback/feedback/', views.feedback_view, name='feedback'),
    path('feedback/feedback_success/', views.feedback_success_view, name='feedback_success'),

    path('team/<int:team_id>/fixtures/', views.team_fixtures, name='team_fixtures'),
    path('team/<int:team_id>/results/', views.team_results, name='team_results'),




   # FAQ Section URLs
    path('pages/FAQ/faq', views.faq_page, name='faq_page'),
    
    
    
    
    path('pages/FAQ/website', views.website_page, name='website_page'),
    path('pages/FAQ/about-us', views.about_us_page, name='about_us_page'),
    path('pages/FAQ/financial', views.financial_page, name='financial_page'),
    path('pages/FAQ/tickets', views.tickets_page, name='tickets_page'),
    path('pages/FAQ/support', views.support_page, name='support_page'),
    path('pages/FAQ/data-privacy', views.data_privacy_page, name='data_privacy_page'),
    path('pages/FAQ/clubs', views.clubs_page, name='clubs_page'),    

    #Open finance URLs
    path('pages/finance/about/', views.finance_about, name='finance-about'),
    path('pages/finance/payment-type/', views.finance_payment_type, name='finance-payment-type'),
    path('pages/finance/sponsor/', views.finance_sponsor, name='finance-sponsor'),
    path('pages/crowdfund-pay/', views.crowdfund_pay, name='crowdfund-pay'),
    path('pages/finance/mpesa-donate', views.mpesa_donate, name='mpesa_donate'),
    path('pages/finance/other-donate', views.other_donate, name='other_donate'),
    path('pages/finance/crowdfund-mpesa', views.crowdfund_mpesa, name='crowdfund_mpesa'),
    path('pages/finance/crowdfund-other', views.crowdfund_other, name='crowdfund_other'),
    
    # View all players URL...
    path('pages/clubs/all-players', views.all_players_view, name='all_players'),

    #Matchday highlights url
    path('pages/matchday-highlights', views.matchday_highlights, name='matchday_highlights'),

    #Custom blog url
    path('pages/blog', views.blog, name='blog'),

    #Tickets url
    path('pages/tickets', views.tickets, name='tickets'),

    #Tickets info url
    path('pages/tickets-pay', views.tickets_pay, name='tickets_pay'),

    #Tickets mpesa url
    path('pages/tickets-mpesa', views.tickets_mpesa, name='tickets_mpesa'),

    #Tickets card url
    path('pages/tickets-card', views.tickets_card, name='tickets_card'),

    #Generated ticket url
    path('ticket_qr/', views.ticket_qr, name='ticket_qr'),

    #Navbar navigation urls
    path('finance/', views.finance, name='finance'),

    path('fixtures/', views.fixtures, name='fixtures'),

    path('blogs/', views.blogs, name='blogs'),

    path('clubs/', views.clubs, name='clubs'),

    #Feedback url
    path('feedback/', views.feedback, name='feedback'),

    #Ligi Open about url
    path('home/ligiopen-about/', views.ligiopen, name='ligiopen'),

]


