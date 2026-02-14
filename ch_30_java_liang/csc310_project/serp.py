import threading
import pandas as pd
import matplotlib.pyplot as plt
from concurrent.futures import ThreadPoolExecutor
import time

# Mock-up of features found in crime-reporting papers
# In a real scenario, you'd use a PDF library like PyMuPDF here.
def analyze_crime_paper(paper_id):
    # Simulating specific features found in different types of systems
    crime_features = {
        "Paper_1": ["GPS Tracking", "Real-time Alerts", "Anonymity"],
        "Paper_2": ["GPS Tracking", "Photo Upload", "Community Map"],
        "Paper_3": ["Anonymity", "Real-time Alerts", "Emergency Dial"],
        "Paper_4": ["GPS Tracking", "Police Dashboard", "Real-time Alerts"],
        "Paper_5": ["Anonymity", "AI Analysis", "GPS Tracking"],
        # Add 10+ papers for the requirement...
    }
    time.sleep(0.5)  # Simulate I/O delay
    return crime_features.get(f"Paper_{paper_id}", ["Basic Reporting"])

def run_task_1():
    all_features = []
    # Multithreaded extraction (10 papers)
    with ThreadPoolExecutor(max_workers=5) as executor:
        results = list(executor.map(analyze_crime_paper, range(1, 11)))
    
    for paper_features in results:
        all_features.extend(paper_features)

    # Count occurrences
    df = pd.Series(all_features).value_counts().reset_index()
    df.columns = ['Feature', 'Frequency']
    
    # Visualization
    plt.figure(figsize=(10,6))
    plt.barh(df['Feature'], df['Frequency'], color='skyblue')
    plt.title("Popularity of Crime-Reporting Features Across Systems")
    plt.xlabel("Number of Systems")
    plt.show()

run_task_1()