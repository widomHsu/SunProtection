1. https://fourtitude.me/api.html

2. getUVI
Get /UVI/UVIByCity/{city}
Get /UVI/UVIByGeo/{lat}&{lon}
response example:
    {
        "success":true,
        "msg":"ok",
        "data":"{\"1709697600\":\"7.6\", \"1709701200\":\"5.16\",....}
    }

3. create reminder
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
4. Get all reminders by email
Get /reminder/get/{email}&{send}
response example:
   {
      "success": true,
      "msg": "ok",
      "data": "[{\"id\":9,\"email\":\"example@gmail.com\",\"startTime\":1809644975,\"endTime\":1909644975,\"lat\":\"37.7749\",\"lon\":\"-122.4194\",\"nextNotifyTime\":1709830826,\"status\":0},
                {\"id\":19,\"email\":\"example@gmail.com\",\"startTime\":1709809226,\"endTime\":1709845226,\"lat\":\"37.7749\",\"lon\":\"-122.4194\",\"nextNotifyTime\":1709845226,\"status\":4}]"
   }
status: 0 for upcoming, 1 for active, 4 for inactive - "upcoming" is just a redundant status, it doesn't work in onBoarding project