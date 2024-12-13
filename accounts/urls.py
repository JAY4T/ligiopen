from django.urls import path
from . import views

urlpatterns = [
    path('register/', views.register_view, name='register'),
    path('login/', views.login_view, name='login'),
    path('logout/', views.logout_view, name='logout'),
    path('coach_dashboard/', views.coach_dashboard, name='coach_dashboard'),
    path('admin_dashboard/', views.admin_dashboard, name='admin_dashboard'),



    
]
