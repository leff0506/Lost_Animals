from flask import Flask
from flask import request, jsonify
from flask_cors import CORS
import database.DBEngine as db
import json
app = Flask(__name__)
# CORS(app, resources={
#     r"/*": {
#         # "origins": ["http://localhost:8000", ],
#         "methods": ["POST","GET"],
#         "allow_headers": ["Content-Type"]
#     }
# })
app.config['JSON_SORT_KEYS'] = False

@app.route('/get_markers/', methods=['GET'])
def get_markers():
    if 'dist_in_metres' not in request.args or 'lat' not in request.args or 'lon' not in request.args:
        return "Error"
    distance_in_metres = float(request.args.get('dist_in_metres'))
    latitude = float(request.args.get('lat'))
    longitude = float(request.args.get('lon'))
    result = {}
    result["data"] =db.get_all_posts_by_dist(distance_in_metres = distance_in_metres, latitude=latitude,longitude=longitude)
    return jsonify(result)

@app.route('/add_post/', methods=['POST'])
def add_post():
    request_data = request.get_json()
    # if got string instead the dict
    if type(request_data) == str:
        request_data = json.loads(request_data)
    # print(request_data)
    if 'description' not in request_data or 'lat' not in request_data or 'lon' not in request_data or 'image' not in request_data:
        return "Error"
    description = str(request_data.get('description'))
    base64_image = str(request_data.get('image'))

    latitude = float(request_data.get('lat'))
    longitude = float(request_data.get('lon'))

    db.add_post(description,latitude,longitude,base64_image)
    print(latitude,longitude)
    result = {}
    return jsonify(result)

@app.route('/')
def ping():
    return "Lost Animals"


if __name__ == "__main__":
    app.run(host='0.0.0.0',debug = True,port=5000)