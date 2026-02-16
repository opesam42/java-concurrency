import os 
import threading
import fitz
from collections import defaultdict
import matplotlib.pyplot as plt


def extract_crime_features(pdf_path):
    features_to_find = ["CNN", "LSTM", "ST-ResNet", "Hotspot Prediction", "Bi-LSTM", "Spatio-temporal Data", "Grid Mapping", "Data Binning", "Data Fusion", "Natural Language Processing", "Evaluation Metrics", "Socio-economic Factors", "Sparsity", "Predictive Policing"]

    found_features = set()
    
    try:
        # get all the text in the pdf
        doc = fitz.open(pdf_path)
        text = ""
        for page in doc:
            text += page.get_text().lower()
        
        # Simple keyword matching
        for feature in features_to_find:
            if feature.lower() in text:
                found_features.add(feature)
        
        return found_features
    except Exception as e:
        print(f"Could not parse {pdf_path}: {e}")
        return []


lock = threading.Lock()
feature_counts = defaultdict(int)

def worker(full_path):

    print(f"Analyzing file: {full_path}")
    features = extract_crime_features(full_path)

    # We use'with lock' to ensure that only one thread updates the 'feature_count' dictionary at a time. To prevent race condition
    with lock:
        for f in features:
            feature_counts[f] += 1

    


dir = 'research_papers/'
threads = []
for filename in os.listdir(dir):
    file_path = os.path.join(dir, filename)
    t = threading.Thread(target=worker, args=(file_path,))
    threads.append(t)
    t.start()



# Wait for all threads to finish as Python Main thread may execute finish and exit the whole program while other child threads are running
for t in threads:
    t.join()
 

# display feature count
sorted_features = sorted(feature_counts.items(), key=lambda x: x[1], reverse=True)
print("\n=== Feature Counts Across All Papers ===")
for feature, count in sorted_features:
    print(f"{feature}: {count}")


# visualize results
features = [f for f, c in sorted_features]
counts = [c for f, c in sorted_features]

plt.figure(figsize=(12,6))
plt.bar(features, counts, color='skyblue')
plt.xticks(rotation=45, ha='right')
plt.ylabel("Number of Papers")
plt.title("Distinctive Features in Crime Reporting Papers")
plt.tight_layout()
plt.show()

