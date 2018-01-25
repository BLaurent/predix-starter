import predix.app
import logging
import json
import os
import glob
import pprint

logging.basicConfig(level=logging.DEBUG)

manifest = predix.app.Manifest()

asset = manifest.get_asset()

schemas = set(glob.glob('./json/*[-]*'))
files = set(glob.glob('./json/*')) - schemas
for file in files:
    with open(file, 'r') as json_data:
        data = json.loads(json_data.read())
        for item in data:
            collection = '/'+file.split('/')[2].split('.')[0]
            print('Ingesting Collection ' + collection)
            asset.post_collection(collection, [item])


# print(asset.get_collections())
#
# pp = pprint.PrettyPrinter(indent=2)
# pp.pprint(asset.get_collection(collection='/engines', filter='RPM=2400'))
# pp.pprint(asset.get_collection(collection='/engines', filter='RPM=2400', fields='horsepower'))
# pp.pprint(asset.get_collection(collection='/locomotives', filter='model=SD70ACe:fleet=/fleets/up-5'))
# pp.pprint(asset.get_collection(collection='/locomotives', filter='engine=/engines/v16-2-5|fleet=/fleets/csx-1'))
# pp.pprint(asset.get_collection(collection='/locomotives', filter='engine=/engines/v16-2-5'))
# ass2 = asset.get_collection(collection='/locomotives', filter='engine=/engines/v16-2-5')
# pp.pprint(ass2)
# asset.patch_collection('locomotives/10', [{'op': 'replace', 'path': '/hqLatLng/lat', 'value': '40.941049'}])
# ass2 = asset.get_collection(collection='/locomotives', filter='engine=/engines/v16-2-5')
# pp.pprint(ass2)
#
# pp.pprint(asset.get_audit())
# pp.pprint(asset.get_audit_changes())