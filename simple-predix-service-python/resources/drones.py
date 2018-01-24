from flask import request
from flask_restful import Resource
import predix.data.timeseries
import logging


class DHandler(Resource):

    def __init__(self):
        self.ts = predix.data.timeseries.TimeSeries()

    def get(self):
        logging.debug(request.headers)
        return 'Hello World!', 200

    def post(self):
        print(request.headers)
        print(request.json)
        return {'hello': 'world'}, 200