
"""Cloud Foundry simple predix-service"""
from flask import Flask, Blueprint, abort, request
from flask_restful import Api
import os
import logging
import requests, requests.auth
from resources import drones
import predix.app
from predix import config
from flask_script import Manager, Server

port = int(os.getenv("PORT", 8080))
blueprint = Blueprint('api_v1', __name__, static_folder='data')

api = Api(blueprint, prefix="/api/v1")
api.add_resource(drones.DHandler, "/drones")

app = Flask(__name__)
app.register_blueprint(blueprint)
manager = Manager(app)

manifest = predix.app.Manifest()

logging.basicConfig(level=logging.INFO)

if config.is_cf_env():
    # @app.before_request
    def before_request():
        logging.debug(request.headers.keys())
        logging.debug(request.endpoint)
        if 'Authorization' in request.headers.keys():
            logging.debug(request.headers['Authorization'][7:])
            if not authorized(request.headers['Authorization'][7:]):
                abort(401)
        elif request.endpoint != 'homepage':
            abort(401)

    def authorized(token):
        client_auth = requests.auth.HTTPBasicAuth(manifest.get_client_id(), manifest.get_client_secret())
        post_data = {"token": token}
        response = requests.post(manifest.get_uaa().uri + "/check_token",
                                 auth=client_auth,
                                 data=post_data)

        logging.debug(response.status_code)
        if response.status_code != 200:
            return False
        else:
            return True

if __name__ == '__main__':
    manager.add_command("runserver", Server(port=port))
    manager.run()
