import requests
import time
import json

class Stopwatch:
    def __init__(self):
        self.start_time = None
        self.end_time = None

    def start(self):
        self.start_time = time.time()

    def stop(self):
        self.end_time = time.time()

    def elapsed_time(self):
        if self.start_time is None:
            raise ValueError("Stopwatch has not been started.")
        else:
            return time.time() - self.start_time

    def reset(self):
        self.start_time = None
        self.end_time = None
        
def get_stream(url):
    s = requests.Session()
    stopwatch = Stopwatch()
    stopwatch.start()
    print(f"Starting now! Time:{stopwatch.elapsed_time():.2f}")
    with s.get(url, headers=None, stream=True) as resp:
        for line in resp.iter_lines():
            if line:
                print(f"{stopwatch.elapsed_time():.2f}: {json.loads(line)}")
                print()
    stopwatch.stop()
url = 'http://localhost:8080/api/flux-endpoint'
get_stream(url)