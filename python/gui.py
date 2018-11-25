import tkinter as tk
from tkinter import ttk
import time
import datetime
from tkinter.filedialog import askopenfilenames
import logging
from tkinter import messagebox


SIZE = 400, 700
font = ("helvetica", 13, "bold")
entry_font = ("helvetica", 11, )
DATE_FORMAT = "20%y_%m_%d"


X1, X2 = 10, 75
WIDTH = 200
HEIGHT = 30
LINING = 40
START = 30


def init_root():
    root = tk.Tk()
    root.geometry("{}x{}".format(*SIZE))
    root.configure(background='white')
    return root

FILES = None

def choose_files():
    FILES = list(askopenfilenames())
    print(type(FILES))
    print(FILES)

def add_files(root):
    Y = START + 0 * LINING
    width = 100
    bt0 = tk.Button(root, text="Choose files", command=choose_files, font=entry_font + ("bold",))
    bt0.place(x=X2 + WIDTH - width, y=Y, height=HEIGHT, width=100)

def add_date(root):
    Y = START + 1 * LINING
    l0 = tk.Label(root, text="Date: ", font=font, background='white')
    l0.place(x=X1, y=Y, height=HEIGHT)

    tf0 = tk.Entry(root, font=entry_font)

    today = time.strftime(DATE_FORMAT)
    tf0.insert(0, today)
    offset = 0
    tf0.place(x=X2 + offset, y=Y, width=WIDTH - offset, height=HEIGHT)
    return tf0


def add_ft(root):
    Y = START + 2 * LINING
    l1 = tk.Label(root, text="FT: ", font=font, background='white')
    l1.place(x=X1, y=Y, height=HEIGHT)

    tf1 = tk.Entry(root, font=entry_font)
    tf1.place(x=X2, y=Y, width=WIDTH, height=HEIGHT)
    return tf1

def add_type(root):
    Y = START + 3 * LINING
    options = ("RLF", "Palubní", "ISLP", "WT", "VAS", "KDVDB")

    l2 = tk.Label(root, text="Type: ", font=font, background='white')
    l2.place(x=X1, y=Y, height=25)

    cb1 = ttk.Combobox(root, font=entry_font)
    cb1['values'] = options
    cb1.place(x=X2, y=Y, width=WIDTH, height=HEIGHT)
    return cb1

def add_marke(root):
    Y = START + 4 * LINING
    options = ("VW", "VW-Nutzfahrzeuge", "Audi", "Seat", "Skoda")

    l3 = tk.Label(root, text="Marke: ", font=font, background='white')
    l3.place(x=X1, y=Y, height=HEIGHT)

    cb2 = ttk.Combobox(root, font=entry_font)
    cb2['values'] = options
    cb2.place(x=X2, y=Y, width=WIDTH, height=HEIGHT)
    return cb2

def add_scheme(root):
    Y = START + 5 * LINING
    options = ("VW 08", "VW 09", "Audi 08")

    l4 = tk.Label(root, text="Scheme: ", font=font, background='white')
    l4.place(x=X1, y=Y, height=HEIGHT)

    cb3 = ttk.Combobox(root, font=entry_font)
    cb3['values'] = options
    offset = 20
    cb3.place(x=X2 + offset, y=Y, width=WIDTH - offset, height=HEIGHT)
    return cb3

def add_output(root):
    Y = START + 6 * LINING
    l5 = tk.Label(root, text="Output file: ", font=font, background='white')
    l5.place(x=X1, y=Y, height=HEIGHT)

    tf2 = tk.Entry(root, font=entry_font)
    tf2.insert(0, 'default text')
    offset = 40
    tf2.place(x=X2 + offset, y=Y, width=WIDTH - offset, height=HEIGHT)
    return tf2

def add_start(root):
    Y = START + 7 * LINING
    width = 100
    bt1 = tk.Button(root, text="Start", command=execute_parser, font=entry_font + ("bold",))
    bt1.place(x=X2 + WIDTH - width, y=Y, height=HEIGHT, width=100)

def parse_entry():
    entry = {'files' : FILES}
    for key, component in components.items():
        value = component.get()
        entry[key] = value
        if value == "":
            message = "{} not selected.".format(key)
            logging.warning(message)
            messagebox.showwarning("Warning", message)
    logging.info("entry: {}".format(entry))
    try:
        d = datetime.datetime.strptime(entry['date'], DATE_FORMAT)
    except Exception as e:
        message = "wrong date format. Date is: {}, but should be in format {}".format(entry["date"], DATE_FORMAT)
        logging.error(message)
        messagebox.showerror("Error", message)
        exit(1)

    logging.info(d)

def execute_parser():
   logging.info("Executing parser")
   parse_entry()

def init_logging():
    logging.basicConfig(
        format='%(asctime)s:%(levelname)s: %(message)s',
        level=0,
        handlers=[logging.FileHandler("/tmp/file.log"), logging.StreamHandler()])



if __name__ == "__main__":
    init_logging()
    root = init_root()

    add_files(root)
    date_entry = add_date(root)
    ft_entry = add_ft(root)
    type_combo = add_type(root)
    marke_combo = add_marke(root)
    scheme_combo = add_scheme(root)
    output_entry = add_output(root)
    add_start(root)

    components = {"date" : date_entry, "ft" : ft_entry, "type" : type_combo, "marke" : marke_combo,
                  "scheme" : scheme_combo, "output" : output_entry}

    root.mainloop()
