from django.urls import path
from rest_framework_simplejwt.views import (
    TokenObtainPairView,  # Login: Generates access & refresh tokens
    TokenRefreshView,     # Refreshes expired access tokens
)
from .views import FixtureListView
from .views import PartnerListCreateAPIView, PartnerRetrieveUpdateDestroyAPIView
from .views import NewsListCreateAPIView,NewsRetrieveUpdateDestroyAPIView  
from .views import FeaturedPlayerListCreateView, FeaturedPlayerDetailView, FeaturedPlayersListView
from .views import FixtureListView, FixtureDetailView, FixtureUpdateView, FixtureDeleteView
from .views import ClubListView
from .views import HighlightsListAPIView
from .views import LiveMatchAPIView
from . import views









urlpatterns = [
    path('api/token/', TokenObtainPairView.as_view(), name='token_obtain_pair'),  # Login
    path('api/token/refresh/', TokenRefreshView.as_view(), name='token_refresh'),  # Refresh token

    #Fixtures
    path('fixtures/', FixtureListView.as_view(), name='fixtures-list'),
    path('fixtures/<int:pk>/', FixtureDetailView.as_view(), name="fixture-detail"),  # Get fixture by ID
    path('fixtures/<int:pk>/update/', FixtureUpdateView.as_view(), name='fixture-update'),
    path('fixtures/<int:pk>/delete/', FixtureDeleteView.as_view(), name='fixture-delete'),



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



    #Clubs
    path('clubs/', ClubListView.as_view(), name='club-list'),


    # NewsHighlight

    path('highlights/', HighlightsListAPIView.as_view(), name='highlights-list'),


    # LiveMatches
    path('live-match/', LiveMatchAPIView.as_view(), name='live-match'),


    # PlayersProfile API
    path('playersprofile/', views.PlayerProfileListCreateView.as_view(), name='playersprofile-list-create'),
    path('playersprofile/<int:pk>/', views.PlayerProfileDetailView.as_view(), name='playersprofile-detail'),

    # # Seasons API
    # path('seasons/', views.SeasonListCreateView.as_view(), name='season-list-create'),
    # path('seasons/<int:pk>/', views.SeasonDetailView.as_view(), name='season-detail'),
]







