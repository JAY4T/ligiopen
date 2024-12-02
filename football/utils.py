from .models import Cart

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
