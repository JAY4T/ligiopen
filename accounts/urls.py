from django.urls import path
from . import views

urlpatterns = [
    path('register/', views.register_view, name='register'),
    path('login/', views.login_view, name='login'),
    path('register-team/', views.register_team, name='register_team'), 
    path('success/', views.success_page, name='success_page'),  # Add this line

   
]
