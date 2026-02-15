import os 
import threading
import requests
import fitz
from collections import defaultdict
import matplotlib.pyplot as plt

crime_papers = [
    {
        "title": "Uniform Crime Reporting Handbook (FBI)",
        "url": "https://ucr.fbi.gov/additional-ucr-publications/ucr_handbook.pdf"
    },
    {
        "title": "Has Crime Reporting Become the Junk Food of News?",
        "url": "https://constructiveinstitute.org/app/uploads/2025/08/Has-crime-reporting-become-the-junk-food-of-news-final.pdf"
    },
    {
        "title": "The Rise of Virtual Vigilantism: Crime Reporting since WWII",
        "url": "https://www.crimeandjustice.org.uk/sites/default/files/09627250108552952.pdf"
    },
    {
        "title": "The Future of Crime Reporting",
        "url": "https://pure.royalholloway.ac.uk/files/30776639/THE_FUTURE_OF_CRIME_REPORTING.pdf"
    },
    {
        "title": "Crime Reporting and Perceived Effects on Its Victims: A Case Study of Ekpoma",
        "url": "https://www.opastpublishers.com/open-access-articles/crime-reporting-and-perceived-effects-on-its-victims-a-case-study-of-ekpoma.pdf"
    },
    {
        "title": "Crime Data Analysis and Prediction Using Machine Learning (WJARR)",
        "url": "https://journalwjarr.com/sites/default/files/fulltext_pdf/WJARR-2025-0385.pdf"
    },
    {
        "title": "Crime Prediction and Mapping Using Machine Learning",
        "url": "https://centerprode.com/ojit/ojit0701/coas.ojit.0701.03023n.pdf"
    },
    {
        "title": "Crime Data Analysis and Prediction Using Machine Learning (IJMTST)",
        "url": "https://ijmtst.com/volume9/issue02/7.IJMTST0902032.pdf"
    },
    {
        "title": "Using Machine Learning Algorithms to Analyze Crime Patterns",
        "url": "https://airccse.org/journal/mlaij/papers/2115mlaij01.pdf"
    },
    {
        "title": "Crime Prediction Using Machine Learning and Deep Learning (SciSpace)",
        "url": "https://scispace.com/pdf/crime-prediction-using-machine-learning-and-deep-learning-a-18t6f4s3.pdf"
    }
]


crime_reporting_papers = [
    {
        "title": "ECRIME – a Web-based Online Crime Reporting System (2026)",
        "url": "https://www.rjwave.org/jaafr/papers/JAAFR2601122.pdf"
    },
    {
        "title": "DEVELOPMENT OF AN AUTOMATED ONLINE CRIME REPORTING AND TRACKING SYSTEM",
        "url": "https://www.ijrti.org/papers/IJRTI2505256.pdf"
    },
    {
        "title": "ONLINE CRIME REPORTING MANAGEMENT SYSTEM POLICE COMPLAINT FIR AND CSR",
        "url": "https://www.ijprems.com/ijprems-paper/online-crime-reporting-management-system-police-complaint-fir-and-csr"
    },
    {
        "title": "Development of Crime Reporting System to Identify Patterns of Crime in Laguna",
        "url": "https://pdfs.semanticscholar.org/e027/22fe4c3186eaa96b9ac0a7d5c86a4bc10149.pdf"
    },
    {
        "title": "Online Crime Reporting: A new threat to police legitimacy",
        "url": "https://pdxscholar.library.pdx.edu/cgi/viewcontent.cgi?article=1123&context=ccj_fac"
    },
    {
        "title": "The Rise of Virtual Vigilantism: Crime Reporting since WWII",
        "url": "https://www.crimeandjustice.org.uk/sites/default/files/09627250108552952.pdf"
    },
    {
        "title": "The Future of Crime Reporting",
        "url": "https://pure.royalholloway.ac.uk/files/30776639/THE_FUTURE_OF_CRIME_REPORTING.pdf"
    },
    {
        "title": "Crime Reporting and Perceived Effects on Its Victims: A Case Study of Ekpoma",
        "url": "https://www.opastpublishers.com/open-access-articles/crime-reporting-and-perceived-effects-on-its-victims-a-case-study-of-ekpoma.pdf"
    },
    {
        "title": "Crime Data Analysis and Prediction Using Machine Learning (WJARR)",
        "url": "https://journalwjarr.com/sites/default/files/fulltext_pdf/WJARR-2025-0385.pdf"
    },
    {
        "title": "Crime Prediction and Mapping Using Machine Learning",
        "url": "https://centerprode.com/ojit/ojit0701/coas.ojit.0701.03023n.pdf"
    },
    {
        "title": "Using Machine Learning Algorithms to Analyze Crime Patterns",
        "url": "https://airccse.org/journal/mlaij/papers/2115mlaij01.pdf"
    },

]

def download_paper(url, filename, directory):
    print(f"Starting download: {filename}")
    try:
        headers = {
            "User-Agent": "Mozilla/5.0"
        }
        response = requests.get(url, timeout=10, headers=headers)
        if response.status_code == 200:
            with open(f"{directory}/{filename}.pdf", 'wb') as f:
                f.write(response.content)
            print(f"Successfully downloaded {filename}")
        else:
            print(f"Failed to download {filename}: Status {response.status_code}")
    except Exception as e:
        print(f"Error downloading {filename}: {e}")


def extract_crime_features(pdf_path):
    features_to_find = ["CNN", "LSTM", "ST-ResNet", "Map", "SOS", "Police Dashboard", "Blockchain"]
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
    # dir = "research_papers"
    # full_path = f"{dir}/{filename}.pdf"
    # download file
    # download_paper(url=url, filename=filename, directory=dir)

    print(f"Analyzing file: {full_path}")
    features = extract_crime_features(full_path)

    # Update global counts safely
    with lock:
        for f in features:
            feature_counts[f] += 1

    

    
# features = extract_crime_features("research_papers/Development of Crime Reporting System to Identify Patterns of Crime in Laguna.pdf")
# print(features)

dir = 'research_papers/'
threads = []
for filename in os.listdir(dir):
    file_path = os.path.join(dir, filename)
    t = threading.Thread(target=worker, args=(file_path,))
    threads.append(t)
    t.start()


# for paper in crime_reporting_papers:
#     t = threading.Thread(target=worker, args=(paper['url'], paper['title']))
#     threads.append(t)
#     t.start()

# Wait for all threads to finish as Python Main thread may execute finish and exit the whole program while other child threads are running
for t in threads:
    t.join()
 
 # ------------------------------
# 8. Sort & Display Feature Counts
# ------------------------------
sorted_features = sorted(feature_counts.items(), key=lambda x: x[1], reverse=True)
print("\n=== Feature Counts Across All Papers ===")
for feature, count in sorted_features:
    print(f"{feature}: {count}")

# ------------------------------
# 9. Visualize Results
# ------------------------------
features = [f for f, c in sorted_features]
counts = [c for f, c in sorted_features]

plt.figure(figsize=(12,6))
plt.bar(features, counts, color='skyblue')
plt.xticks(rotation=45, ha='right')
plt.ylabel("Number of Papers")
plt.title("Distinctive Features in Crime Reporting Papers")
plt.tight_layout()
plt.show()

