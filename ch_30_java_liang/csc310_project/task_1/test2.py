import os
import threading
import fitz  # PyMuPDF
import matplotlib.pyplot as plt
from collections import defaultdict

# ------------------------------
# 1. Papers (Assume already downloaded PDFs)
# ------------------------------
PDF_DIR = "research_papers"
papers = [f for f in os.listdir(PDF_DIR) if f.endswith(".pdf")]

# ------------------------------
# 2. Sections to extract
# ------------------------------
SECTIONS = ["Introduction", "Abstract", "Literature Review", "Methodology", "Proposed Framework", "Ethical Consideration" "Related Work", "Methodology", "Experiments", "Results", "Conclusion", "Future Work"]

# ------------------------------
# 3. Thread-safe global count
# ------------------------------
section_counts = defaultdict(int)
lock = threading.Lock()

# ------------------------------
# 4. Extract sections from a PDF
# ------------------------------
def extract_sections(pdf_path):
    found = set()
    try:
        doc = fitz.open(pdf_path)
        text = ""
        for page in doc:
            text += page.get_text()
        text_lower = text.lower()
        for section in SECTIONS:
            if section.lower() in text_lower:
                found.add(section)
    except Exception as e:
        print(f"[ERROR] Could not parse {pdf_path}: {e}")
    return found

# ------------------------------
# 5. Worker function for threads
# ------------------------------
def worker(pdf_file):
    full_path = os.path.join(PDF_DIR, pdf_file)
    found_sections = extract_sections(full_path)
    with lock:
        for s in found_sections:
            section_counts[s] += 1
    print(f"[DONE] {pdf_file} -> Sections: {found_sections}")

# ------------------------------
# 6. Start threads
# ------------------------------
threads = []
for pdf_file in papers:
    t = threading.Thread(target=worker, args=(pdf_file,))
    threads.append(t)
    t.start()

for t in threads:
    t.join()

# ------------------------------
# 7. Rank and display sections
# ------------------------------
sorted_sections = sorted(section_counts.items(), key=lambda x: x[1], reverse=True)
print("\n=== Section Counts Across Papers ===")
for section, count in sorted_sections:
    print(f"{section}: {count}")

# ------------------------------
# 8. Visualize using matplotlib
# ------------------------------
sections = [s for s, c in sorted_sections]
counts = [c for s, c in sorted_sections]

plt.figure(figsize=(10,6))
plt.bar(sections, counts, color='skyblue')
plt.xticks(rotation=45, ha='right')
plt.ylabel("Number of Papers")
plt.title("Sub-Sections Presence Across Crime Reporting Deep Learning Papers")
plt.tight_layout()
plt.show()
