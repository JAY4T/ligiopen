from django.http import HttpResponsePermanentRedirect

class RemoveSlashMiddleware:
    def __init__(self, get_response):
        self.get_response = get_response

    def __call__(self, request):
        if request.path.endswith('/') and request.path != '/':
            return HttpResponsePermanentRedirect(request.path[:-1])
        return self.get_response(request)
