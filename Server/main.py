from flask import Flask
from flask import request, jsonify
from flask_cors import CORS
import database.DBEngine as db
app = Flask(__name__)
# CORS(app, resources={
#     r"/*": {
#         # "origins": ["http://localhost:8000", "https://blor-brain.appspot.com"],
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


@app.route('/')
def ping():
    return "Lost Animals"


if __name__ == "__main__":
    app.run(host='0.0.0.0',debug = True,port=5000)