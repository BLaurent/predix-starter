from flask import current_app, request
from flask_restful import Resource
import predix.data.blobstore
import logging
import os
from werkzeug.utils import secure_filename
from datetime import datetime
import json


ALLOWED_EXTENSIONS = os.getenv('ALLOWED_EXTENSIONS', {'txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif'})


class DateTimeEncoder(json.JSONEncoder):
    def default(self, o):
        if isinstance(o, datetime):
            return o.isoformat()

class DHandler(Resource):

    def __init__(self):
        self.bs = predix.data.blobstore.BlobStore()

    def _allowed_file(self, filename):
        return '.' in filename and \
               filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS

    def get(self):
        logging.debug(request.headers)
        return json.loads(json.dumps(self.bs.list_objects()['Contents'], cls=DateTimeEncoder)), 200

    def post(self):
        logging.debug(request.headers)
        logging.debug(request.json)
        for file in dict(request.files)['files[]']:
            if self._allowed_file(file.filename):
                filename = secure_filename(file.filename)
                path = os.path.join(current_app.config['UPLOAD_FOLDER'], filename)
                file.save(path)
                return self.bs.upload_file(path, file.filename), 200
            else:
                return file.filename + ' not ingested. File type not allowed.'