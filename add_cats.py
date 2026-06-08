import urllib.request
import urllib.error
import json

data = json.dumps({'email': 'admin@ims.com', 'password': 'Raguladmin@7'}).encode('utf-8')
req = urllib.request.Request('http://localhost:8083/api/v1/auth/login', data=data, headers={'Content-Type': 'application/json'})
try:
    resp = urllib.request.urlopen(req)
    token = json.loads(resp.read().decode('utf-8'))['token']
    print('Token received')
    
    categories = [
        {'name': 'Electronics', 'description': 'Devices', 'active_status': 'active'},
        {'name': 'Office Supplies', 'description': 'Stationery', 'active_status': 'active'},
        {'name': 'Furniture', 'description': 'Desks', 'active_status': 'active'}
    ]
    for c in categories:
        creq = urllib.request.Request('http://localhost:8083/api/v1/categories/newcategory', data=json.dumps(c).encode('utf-8'), headers={'Content-Type': 'application/json', 'Authorization': f'Bearer {token}'})
        cresp = urllib.request.urlopen(creq)
        print(f'Added {c["name"]}')
except urllib.error.HTTPError as e:
    print(f'Error: {e.code} {e.reason}')
    print(e.read().decode('utf-8'))
