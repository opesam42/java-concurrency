import os
import threading
import fitz  # PyMuPDF
import matplotlib.pyplot as plt
from collections import defaultdict


PDF_DIR = "research_papers"
papers = [f for f in os.listdir(PDF_DIR) if f.endswith(".pdf")]


SUB_HEADINGS = ["Introduction", "Abstract", "Literature Review", "Related Work", "Methodology", "Proposed Framework", "Data Set and Preprocessing", "Ethical Consideration" "Related Work", "Methodology", "Experiments", "Results", "Conclusion", "Future Work", "References"]


subheading_counts = defaultdict(int)
lock = threading.Lock()

# extract the heading from the pdf
def extract_subheadings(pdf_path):
    found = set()
    try:
        doc = fitz.open(pdf_path)
        text = ""
        for page in doc:
            text += page.get_text()
        text_lower = text.lower()
        for sub_heading in SUB_HEADINGS:
            if sub_heading.lower() in text_lower:
                found.add(sub_heading)
    except Exception as e:
        print(f"[ERROR] Could not parse {pdf_path}: {e}")
    return found


def worker(pdf_file):
    full_path = os.path.join(PDF_DIR, pdf_file)
    found_subheading = extract_subheadings(full_path)
    # We use'with lock' to ensure that only one thread updates the 'feature_count' dictionary at a time. To prevent race condition
    with lock:
        for s in found_subheading:
            subheading_counts[s] += 1
    print(f"[DONE] {pdf_file} -> Sub-Heading: {found_subheading}")

# start threads
threads = []
for pdf_file in papers:
    t = threading.Thread(target=worker, args=(pdf_file,))
    threads.append(t)
    t.start()

for t in threads:
    t.join()


# display result
sorted_sub_headiings = sorted(subheading_counts.items(), key=lambda x: x[1], reverse=True)
print("\n=== Sub-headings Counts Across Papers ===")
for sub_headiings, count in sorted_sub_headiings:
    print(f"{sub_headiings}: {count}")


# visualize result
sub_headiings = [s for s, c in sorted_sub_headiings]
counts = [c for s, c in sorted_sub_headiings]

plt.figure(figsize=(10,6))
plt.bar(sub_headiings, counts, color='skyblue')
plt.xticks(rotation=45, ha='right')
plt.ylabel("Number of Papers")
plt.title("Sub-Headings Presence Across Crime Reporting Deep Learning Papers")
plt.tight_layout()
plt.show()
