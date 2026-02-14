import threading
import requests
import fitz


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
    }
]

def download_paper(url, filename, directory):
    print(f"Starting download: {filename}")
    try:
        response = requests.get(url, timeout=10)
        if response.status_code == 200:
            with open(f"{directory}/{filename}.pdf", 'wb') as f:
                f.write(response.content)
            print(f"Successfully downloaded {filename}")
        else:
            print(f"Failed to download {filename}: Status {response.status_code}")
    except Exception as e:
        print(f"Error downloading {filename}: {e}")


def extract_crime_features(pdf_path):
    features_to_find = ["GPS", "SMS", "Anonymity", "Map", "SOS", "Police Dashboard", "Blockchain"]
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

def worker(url, filename):
    dir = "research_papers"
    full_path = f"{dir}/{filename}.pdf"
    # download file
    download_paper(url=url, filename=filename, directory=dir)

    print(f"Analyzing file: {full_path}")
    features = extract_crime_features(full_path)

    print(f"\n {features} \n")

    
# features = extract_crime_features("research_papers/Development of Crime Reporting System to Identify Patterns of Crime in Laguna.pdf")
# print(features)


threads = []
for paper in crime_reporting_papers:
    t = threading.Thread(target=worker, args=(paper['url'], paper['title']))
    threads.append(t)
    t.start()

# Wait for all threads to finish as Python Main thread may execute finish and exit the whole program while other child threads are running
for t in threads:
    t.join()
 

""" url = "https://pdxscholar.library.pdx.edu/cgi/viewcontent.cgi?article=1123&context=ccj_fac"
paper_name = "Online Crime Reporting: A new threat to police Online Crime Reporting: A new threat to police legitimacy"
download_paper(url, paper_name) """