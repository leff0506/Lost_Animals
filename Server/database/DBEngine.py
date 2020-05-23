import pymysql.cursors
import math

config = {  # настройки для подключения к бд логин пароль имя бд
    'user': 'root',
    'password': '',
    'host': 'localhost',
    'database': 'lost_animals',
    'charset': 'utf8mb4',
    'cursorclass': pymysql.cursors.DictCursor
}


def execute_with_commit(sql):  # функция выполняющая запрос sql с commit транзакции (вносит изменения)
    # print(sql)
    connection = pymysql.connect(**config)
    cursor = connection.cursor()
    cursor.execute(sql)
    connection.commit()
    cursor.close()
    connection.close()


def execute(sql):  # функция которая просто выпоняет sql запрос выборки поиска и тд (без изменения данных)
    # print(sql)
    connection = pymysql.connect(**config)
    cursor = connection.cursor()
    cursor.execute(sql)
    answer = cursor.fetchall()
    return answer


def select(what="*", origin="",
           where=""):  # функция выбора принимает (что выбрать) (откуда выбрать) и (с каким условием выбрать)
    if where == "":
        sql = "SELECT " + what + " FROM " + origin + " "
        return execute(sql)
    else:
        sql = "SELECT " + what + " FROM " + origin + " " + "WHERE " + where
        return execute(sql)

def measure(lat1, lon1, lat2, lon2):  # generally used geo measurement function
    R = 6378.137 # Radius of earth in KM
    dLat = lat2 * math.pi / 180 - lat1 * math.pi / 180
    dLon = lon2 * math.pi / 180 - lon1 * math.pi / 180
    a = math.sin(dLat/2) * math.sin(dLat/2) + math.cos(lat1 * math.pi / 180) * math.cos(lat2 * math.pi / 180) * math.sin(dLon/2) * math.sin(dLon/2)
    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1-a))
    d = R * c
    return int(d * 1000) # meters

def get_all_posts_by_dist(distance_in_metres, latitude, longitude):
    response = select(origin="posts")
    result = []
    for resp in response:
        lat = resp["latitude"]
        lon = resp["longitude"]
        if measure(lat1=latitude, lon1 = longitude, lat2 = lat, lon2 = lon) <= distance_in_metres:
            result.append(resp)
    return result

def add_post(description, latitude,longitude,image_base_64):
    request = "INSERT INTO `posts` (`id`, `latitude`, `longitude`, `url`, `description`) VALUES (NULL, '{}', '{}', '{}','{}');".format(latitude,longitude,image_base_64,description)
    execute_with_commit(request)