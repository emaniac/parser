import os
import time
import yaml
import logic
import logging
import datetime
from platform import platform
from glob import glob

import tkinter as tk
from tkinter import ttk
from tkinter import messagebox
from tkinter import filedialog
from tkinter.filedialog import askopenfilenames


WIN7 = "Windows-7" in platform()
WIN10 = "Windows-10" in platform()
LINUX = "Linux" in platform()


SCALE = 1
SIZE = SCALE * 500, SCALE * 800
font = ("helvetica", SCALE * 13, "bold")
entry_font = "helvetica", SCALE * 11
DATE_FORMAT = "%d.%m.20%y"

X1, X2 = SCALE * 10, SCALE * 75
WIDTH = SCALE * 400
HEIGHT = SCALE * 30
LINING = SCALE * 40
START = SCALE * 50

DEFAULT_MAGIC_DIR = ""
DEFAULT_TXT_DIR = ""

def warning(message):
    logging.warning(message)
    messagebox.showwarning("Warning", message)

def error(message):
    logging.error(message)
    messagebox.showerror("Error", message)

def info(message):
    logging.info(message)
    messagebox.showinfo("Info", message)


def init_root():
    root = tk.Tk()
    root.tk.call('tk', 'scaling', SCALE)
    root.geometry("{}x{}".format(*SIZE))
    root.configure(background='white')
    return root

FILES = None

def set_default_dirs():
    """Se the default directories from default.yaml"""
    global DEFAULT_MAGIC_DIR, DEFAULT_TXT_DIR
    dir_path = os.path.dirname(os.path.realpath(__file__))
    path = os.path.join(dir_path, "default.yaml")
    system = "linux" if LINUX else "windows"

    # Read the yaml file
    if os.path.isfile(path):
        with open(path, 'r') as f:
            try:
                data = yaml.safe_load(f)
            except Exception as e:
                error(e)
        if os.path.isdir(data['default_magic'][system]):
            DEFAULT_MAGIC_DIR = data['default_magic'][system]
        if os.path.isdir(data['default_txt'][system]):
            DEFAULT_TXT_DIR = data['default_txt'][system]
        logging.info("DEFAULT_MAGIC_DIR: {}, DEFAULT_TXT_DIR: {}"
            .format(DEFAULT_MAGIC_DIR, DEFAULT_TXT_DIR))
    else:
        warning("Could not find {}. Create file \'default.yaml\' in the same directory as gui.py "
                "containing only one line with the default directory".format(file))
        logging.warning(e)


def choose_files():
    global FILES
    FILES = list(askopenfilenames(initialdir=DEFAULT_MAGIC_DIR))
    logging.info("selected files: {}".format(FILES))
    if FILES:
        files_dir = os.path.dirname(FILES[0])
        files_dir = os.path.basename(files_dir)
        # ft_entry.insert(0, files_dir)

        names = [os.path.basename(e) for e in FILES]
        names = "\n".join(names)
        logging.info("selected files {}".format(names))
        files_label.delete('1.0', tk.END)
        files_label.insert(1.0, names)

def choose_txt_files():
    folder_selected = filedialog.askdirectory(initialdir=DEFAULT_TXT_DIR)
    regex = os.path.join(folder_selected, "*.txt")
    files = glob(regex)
    if files:
        text = [open(f).read().strip() for f in files]
        line = "\n======================================\n"
        text = line.join(text)
        export = os.path.join(folder_selected, os.path.basename(folder_selected) + '.txt')
        with open(export, 'w') as f:
            f.write(text)
        files_label2.delete('1.0', tk.END)
        log = ['Processed_files:'] + files + ['Saved_as'] + [export]
        files_label2.insert(1.0, log)
    else:
        warning('Did not find any txt files.')


def add_icon(root):
    root_path = os.path.dirname(os.path.dirname(os.path.realpath(__file__)))
    icon_path = os.path.join(root_path, "icons", "icon_black_small.png")

    icon = tk.Label(root,compound="top")
    icon.lenna_image_png = tk.PhotoImage(file=icon_path)
    icon['image'] = icon.lenna_image_png
    icon.place(x=X1, y=START - SCALE * 20, height=SCALE * 50, width=SCALE * 86)

    assert os.path.isfile(icon_path)



def add_files(root):
    Y = START + 0 * LINING
    width = SCALE * 100
    bt0 = tk.Button(root, text="Choose files", command=choose_files, font=entry_font + ("bold",))
    bt0.place(x=X2 + WIDTH - width, y=Y, height=HEIGHT, width=SCALE * 100)

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
    options = ("RLF", "Palubn\u00ed", "ISLP", "WT", "VAS", "KDVDB")

    l2 = tk.Label(root, text="Type: ", font=font, background='white')
    l2.place(x=X1, y=Y, height=25)

    cb1 = ttk.Combobox(root, font=entry_font)
    cb1['values'] = options
    cb1.current(0)
    cb1.place(x=X2, y=Y, width=WIDTH, height=HEIGHT)
    return cb1

def add_marke(root):
    Y = START + 4 * LINING
    options = ("VW", "VW-Nutzfahrzeuge", "Audi", "Seat", "Skoda")

    l3 = tk.Label(root, text="Marke: ", font=font, background='white')
    l3.place(x=X1, y=Y, height=HEIGHT)

    cb2 = ttk.Combobox(root, font=entry_font)
    cb2['values'] = options
    cb2.current(0)
    cb2.place(x=X2, y=Y, width=WIDTH, height=HEIGHT)
    return cb2

def add_scheme(root):
    Y = START + 5 * LINING
    options = ("VW 08", "VW 09", "Audi 08")

    l4 = tk.Label(root, text="Scheme: ", font=font, background='white')
    l4.place(x=X1, y=Y, height=HEIGHT)

    cb3 = ttk.Combobox(root, font=entry_font)
    cb3['values'] = options
    cb3.current(0)
    offset = 20
    cb3.place(x=X2 + offset, y=Y, width=WIDTH - offset, height=HEIGHT)
    return cb3

def add_output(root):
    Y = START + 6 * LINING
    l5 = tk.Label(root, text="Output file: ", font=font, background='white')
    l5.place(x=X1, y=Y, height=HEIGHT)

    tf2 = tk.Entry(root, font=entry_font)
    tf2.insert(0, "output.dar")
    offset = 40
    tf2.place(x=X2 + offset, y=Y, width=WIDTH - offset, height=HEIGHT)
    return tf2

def add_start(root):
    Y = START + 7 * LINING
    width = 100
    bt1 = tk.Button(root, text="Start", command=execute_parser, font=entry_font + ("bold",))
    bt1.place(x=X2 + WIDTH - width, y=Y, height=HEIGHT, width=100)

def add_files_label(root):
    Y = START + 8 * LINING

    width, height = WIDTH, 400

    l6 = tk.Label(root, text="Files: ", font=font, background='white')
    l6.place(x=X1, y=Y, height=HEIGHT)

    txt_frm = tk.Frame(root, width=width, height=height)
    txt_frm.place(x=X2, y=Y, height = height, width = WIDTH)

    # create a Text widget
    txt = tk.Text(txt_frm, borderwidth=3, relief="sunken")
    txt.config(font=("consolas", 12), undo=True, wrap='word')
    txt.place(x=0, y=0, width = width, height = height)
    return txt

def parse_entry():
    entry = {'files' : FILES}
    if not FILES:
        message = "FILES not selected: {}".format(FILES)
        logging.warning(message)
        messagebox.showwarning("Warning", message)
        return False
    for key, component in components.items():
        value = component.get()
        entry[key] = value
        if value == "":
            message = "{} not selected.".format(key)
            logging.warning(message)
            messagebox.showwarning("Warning", message)
            return False
    logging.info("entry: {}".format(entry))
    try:
        d = datetime.datetime.strptime(entry['date'], DATE_FORMAT)
    except Exception as e:
        message = "wrong date format. Date is: {}, but should be in format {}".format(entry["date"], DATE_FORMAT)
        logging.error(message)
        messagebox.showerror("Error", message)
        return False
    return entry

def execute_parser():
   logging.info("Executing parser")
   entry = parse_entry()
   if not warning:
       error("Did not correctly parse input from user.")
   else:
       logging.info("continuing with {}".format(entry))
       logic.main(entry)

def init_logging():
    def init_logging():
    dest = os.path.join(os.path.dirname(os.path.abspath(__file__)), 'log.txt')
    logging.basicConfig(
        format='%(asctime)s:%(levelname)s: %(message)s',
        level=0,
        handlers=[logging.FileHandler(dest), logging.StreamHandler()])

def add_dir(root):
    Y = START + 0 * LINING
    width = SCALE * 100
    bt0 = tk.Button(root, text="Choose directory", command=choose_txt_files, font=entry_font + ("bold",))
    bt0.place(x=X1, y=START, height=HEIGHT, width = SCALE * 100)



def main():
    global components
    global date_entry, ft_entry, type_combo, scheme_combo, output_entry, files_label, files_label2
    set_default_dirs()

    # Create tabs
    root = init_root()
    tab_parent = ttk.Notebook(root)
    tab1 = ttk.Frame(tab_parent)
    tab2 = ttk.Frame(tab_parent)
    tab_parent.add(tab1, text="Magic")
    tab_parent.add(tab2, text="Parser")
    tab_parent.pack(expand=1, fill='both')

    # Magic
    add_icon(tab1)
    add_files(tab1)
    date_entry = add_date(tab1)
    ft_entry = add_ft(tab1)
    type_combo = add_type(tab1)
    marke_combo = add_marke(tab1)
    scheme_combo = add_scheme(tab1)
    output_entry = add_output(tab1)
    add_start(tab1)
    files_label = add_files_label(tab1)

    # Parser
    _ = add_dir(tab2)
    files_label2 = add_files_label(tab2)

    components = {"date" : date_entry, "ft" : ft_entry, "type" : type_combo, "marke" : marke_combo,
                  "scheme" : scheme_combo, "output" : output_entry}
    root.mainloop()

if __name__ == "__main__":
    init_logging()
    try:
        main()
    except Exception as e:
        error(e)
