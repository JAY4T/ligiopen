from rest_framework.generics import ListCreateAPIView, RetrieveAPIView, UpdateAPIView, DestroyAPIView

from rest_framework import generics
from .models import Fixture
from .serializers import FixtureSerializer
from django.views.decorators.csrf import csrf_exempt
from django.utils.decorators import method_decorator
from rest_framework_simplejwt.views import TokenObtainPairView
from .models import Partner
from .serializers import PartnerSerializer
from .models import News
from .serializers import NewsSerializer
from .models import FeaturedPlayer
from .serializers import FeaturedPlayerSerializer
from rest_framework.generics import ListAPIView, RetrieveAPIView
from rest_framework.permissions import IsAdminUser, AllowAny




# get all fixtures

class FixtureListView(ListCreateAPIView):
    queryset = Fixture.objects.all()
    serializer_class = FixtureSerializer

# get only one fixture

class FixtureDetailView(RetrieveAPIView):
    queryset = Fixture.objects.all()
    serializer_class = FixtureSerializer
    permission_classes = [AllowAny]  
    
    
    
    
    
    def get_permissions(self):
        if self.request.method == "POST":
            return [IsAdminUser()]  
        return [AllowAny()]  



# Allow only admins to update a fixture
class FixtureUpdateView(UpdateAPIView):
    queryset = Fixture.objects.all()
    serializer_class = FixtureSerializer
    permission_classes = [IsAdminUser]  # Only admins can update fixtures

# Allow only admins to delete a fixture
class FixtureDeleteView(DestroyAPIView):
    queryset = Fixture.objects.all()
    serializer_class = FixtureSerializer
    permission_classes = [IsAdminUser]  # Only admins can delete fixtures

@method_decorator(csrf_exempt, name='dispatch')
class CustomTokenObtainPairView(TokenObtainPairView):
    pass




#  Partners
class PartnerListCreateAPIView(generics.ListCreateAPIView):
    queryset = Partner.objects.all()
    serializer_class = PartnerSerializer

class PartnerRetrieveUpdateDestroyAPIView(generics.RetrieveUpdateDestroyAPIView):
    queryset = Partner.objects.all()
    serializer_class = PartnerSerializer


class NewsListCreateAPIView(generics.ListCreateAPIView):
    queryset = News.objects.all().order_by("-created_at")
    serializer_class = NewsSerializer

class NewsRetrieveUpdateDestroyAPIView(generics.RetrieveUpdateDestroyAPIView):
    queryset = News.objects.all()
    serializer_class = NewsSerializer



# List all players or create a new one
class FeaturedPlayerListCreateView(generics.ListCreateAPIView):
    queryset = FeaturedPlayer.objects.all()
    serializer_class = FeaturedPlayerSerializer

# Retrieve, update, or delete a specific player
class FeaturedPlayerDetailView(generics.RetrieveUpdateDestroyAPIView):
    queryset = FeaturedPlayer.objects.all()
    serializer_class = FeaturedPlayerSerializer

# Get only featured players
class FeaturedPlayersListView(generics.ListAPIView):
    queryset = FeaturedPlayer.objects.filter(is_featured=True)
    serializer_class = FeaturedPlayerSerializer


