from django.urls import path
from rest_framework_simplejwt.views import (
    TokenObtainPairView,  # Login: Generates access & refresh tokens
    TokenRefreshView,     # Refreshes expired access tokens
)
from .views import FixtureListView
from .views import PartnerListCreateAPIView, PartnerRetrieveUpdateDestroyAPIView
from .views import NewsListCreateAPIView,NewsRetrieveUpdateDestroyAPIView  
from .views import FeaturedPlayerListCreateView, FeaturedPlayerDetailView, FeaturedPlayersListView
from .views import FixtureListView, FixtureDetailView




urlpatterns = [
    path('api/token/', TokenObtainPairView.as_view(), name='token_obtain_pair'),  # Login
    path('api/token/refresh/', TokenRefreshView.as_view(), name='token_refresh'),  # Refresh token

    #Fixtures
    path('fixtures/', FixtureListView.as_view(), name='fixtures-list'),
    path("fixtures/<int:pk>/", FixtureDetailView.as_view(), name="fixture-detail"),  # Get fixture by ID



    # Partners
    path("partners/", PartnerListCreateAPIView.as_view(), name="partner-list"),
    path("partners/<int:pk>/", PartnerRetrieveUpdateDestroyAPIView.as_view(), name="partner-detail"),


    #News
    path('news/', NewsListCreateAPIView.as_view(), name='news-list-create'),
    path('news/<int:pk>/', NewsRetrieveUpdateDestroyAPIView.as_view(), name='news-detail'),



    #FeaturedPlayers
    path('players/', FeaturedPlayerListCreateView.as_view(), name='players-list-create'),
    path('players/<int:pk>/', FeaturedPlayerDetailView.as_view(), name='player-detail'),
    path('players/featured/', FeaturedPlayersListView.as_view(), name='featured-players'),
]



