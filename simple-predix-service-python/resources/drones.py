from flask import request
from flask_restful import Resource
import predix.data.timeseries
import logging


class DHandler(Resource):

    def __init__(self):
        self.ts = predix.data.timeseries.TimeSeries()

    def get(self):
        retval = self.ts.get_datapoints(tags=request.args['tag'], start=int(request.args['startDate']), end=int(request.args['endDate']))
        return retval, 200

    def post(self):
        logging.debug(request.headers)
        logging.debug(request.json)
        data = request.json
        self.ts.send(data['type']+'-'+data['deviceId'], data['value'])
        return 200

