from .models import Cart
import os
import markdown2
import yaml

def get_or_create_cart(user):
    cart, created = Cart.objects.get_or_create(user=user)
    return cart


# utils.py

def add_to_cart(request, item):
    # Initialize the cart if not already present
    cart = request.session.get('cart', {})

    # Add item to the cart (increment quantity if item already exists in cart)
    if str(item.id) in cart:
        cart[str(item.id)]['quantity'] += 1
    else:
        cart[str(item.id)] = {'name': item.name, 'price': item.price, 'quantity': 1, 'image_url': item.image.url}

    # Save the updated cart back to the session
    request.session['cart'] = cart





def load_news_items():
    news_items = []
    news_dir = os.path.join(os.path.dirname(__file__), "news")
    for filename in os.listdir(news_dir):
        if filename.endswith(".md"):
            with open(os.path.join(news_dir, filename), 'r') as file:
                content = file.read()
                metadata, body = content.split('---', 2)[1:]
                metadata_dict = yaml.safe_load(metadata)
                html_content = markdown2.markdown(body)
                news_items.append({**metadata_dict, "content": html_content})
    return sorted(news_items, key=lambda x: x['date'], reverse=True)  # Sort by date, latest first
