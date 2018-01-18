from flask import request
from flask_restful import Resource

class DHandler(Resource):
    def get(self):
        print(request.headers)
        return 'Hello World!', 200

    def post(self):
        print(request.headers)
        print(request.json)
        return {'hello': 'world'}, 200