1. getUVI
Get /UVI/UVIByCity/{city}
Get /UVI/UVIByGeo/{lat}&{lon}
response example:
    {
        "success":true,
        "msg":"ok",
        "data":"{\"1709697600\":\"7.6\",\"1709701200\":\"5.16\",....}
    }

2. create reminder
Post /reminder/create
request example:
    {
        "email": "example@gmail.com",
        "startTime": 1709644975,
        "endTime": 1809644975,
        "lat": "37.7749",
        "lon": "-122.4194"
    }
response example:
    {
        "success": true,
        "msg": "ok",
        "data": null
    }