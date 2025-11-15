from urllib.request import Request, urlopen
from urllib.error import URLError, HTTPError
from datetime import date, timedelta, datetime
import json

def pullAnswers():
    
    endDate = datetime.now().date()
    startDate = date(2021, 10, 10)
    delta = timedelta(days=1)
    while startDate <= endDate:
        #print(startDate.strftime("%Y-%m-%d"))
        year = startDate.year
        month = startDate.month
        if month < 10:
            month = "0" + str(month)
        day = startDate.day
        if day < 10:
            day = "0" + str(day)
        dateString = str(year) + "-" + str(month) + "-" + str(day)
        fetchResponse(dateString)
        startDate += delta
        

def fetchResponse(date):
    solutions = []
    req = Request("https://www.nytimes.com/svc/wordle/v2/" + date + ".json")
    try:
        response = urlopen(req)
        data = json.load(response)
        print(data['solution'] + "  " + date)
        solutions.append(data['solution'])
        output.write(data['solution'])
        output.write("\n")
        
    except HTTPError as e:
        print("HTTP ERROR: " + str(e.code) + " (" + date + ")")
    except URLError as e:
        print("URL ERROR: " + str(e.reason) + " (" + date + ")")

output = open("wordle_answers.txt", "w")
pullAnswers()
output.close()
