from rest_framework.generics import ListCreateAPIView, RetrieveAPIView, UpdateAPIView, DestroyAPIView

from rest_framework import generics, filters
from rest_framework.response import Response  
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
from .models import Club
from .serializers import ClubSerializer
from rest_framework import status
from .models import Highlight
from .serializers import HighlightSerializer
from rest_framework.views import APIView
from .models import LiveMatch
from .serializers import LiveMatchSerializer
from .models import PlayerProfile #Season
from .serializers import PlayerProfileSerializer #SeasonSerializer
from django_filters.rest_framework import DjangoFilterBackend








# get all fixtures
class FixtureListView(ListCreateAPIView):
    queryset = Fixture.objects.all()
    serializer_class = FixtureSerializer

# get only one fixture
class FixtureDetailView(RetrieveAPIView):
    queryset = Fixture.objects.all()
    serializer_class = FixtureSerializer

# Allow updating a fixture (no permission restriction)
class FixtureUpdateView(UpdateAPIView):
    queryset = Fixture.objects.all()
    serializer_class = FixtureSerializer

# Allow deleting a fixture (no permission restriction)
class FixtureDeleteView(DestroyAPIView):
    queryset = Fixture.objects.all()
    serializer_class = FixtureSerializer

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




class ClubListView(ListCreateAPIView):
    """
    API view to list all clubs and create new ones.
    """
    queryset = Club.objects.all()
    serializer_class = ClubSerializer

    def perform_create(self, serializer):
        """
        Create a new club while ensuring the name field is provided.
        """
        club_name = self.request.data.get("name")
        if not club_name:
            raise ValueError("The 'name' field is required.")

        serializer.save()

class HighlightsListAPIView(APIView):
    def get(self, request):
        highlights = News.objects.order_by('-created_at')  # Use correct model and field
        serializer = NewsSerializer(highlights, many=True)
        return Response(serializer.data)



class LiveMatchAPIView(APIView):
    def get(self, request):
        live_matches = LiveMatch.objects.filter(is_live=True)
        if live_matches.exists():
            serializer = LiveMatchSerializer(live_matches, many=True)
            return Response(serializer.data)
        return Response({"error": "No live match available."}, status=status.HTTP_404_NOT_FOUND)

    def post(self, request):
        serializer = LiveMatchSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()  # is_live can be True/False from input
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)




# Player Views
class PlayerProfileListCreateView(generics.ListCreateAPIView):
    queryset = PlayerProfile.objects.all()
    serializer_class = PlayerProfileSerializer
    search_fields = ['name', 'position', 'club__name']
    filter_backends = [filters.SearchFilter]


    # Optional: you can add filters for club or season if needed.

class PlayerProfileDetailView(generics.RetrieveUpdateDestroyAPIView):
    queryset = PlayerProfile.objects.all()
    serializer_class = PlayerProfileSerializer

# # Season Views
# class SeasonListCreateView(generics.ListCreateAPIView):
#     queryset = Season.objects.all()
#     serializer_class = SeasonSerializer
#
# class SeasonDetailView(generics.RetrieveUpdateDestroyAPIView):
#     queryset = Season.objects.all()
#     serializer_class = SeasonSerializer