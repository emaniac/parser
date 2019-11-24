import os
import csv
from gui import warning, error, info, init_logging
import logging
import yattag


def get(d, n1, n2):
    return d[n1.lower()] if n1.lower() in d else d[n2.lower()]


def main(entry):
    logging.info("Parsing started,")
    FILES = entry['files']
    DATE = entry['date']
    FT = entry['ft']
    TYPE = entry['type']
    MARKE = entry['marke']
    SCHEME = entry['scheme']
    OUTPUT = entry['output']
    output = os.path.join(os.path.dirname(FILES[0]), OUTPUT)

    header = "<?xml version='1.0' encoding='windows-1250' ?>"
    customer = "Porsche Èeská republika s.r.o."

    doc, tag, text = yattag.Doc().tagtext()

    with tag('Import'):
        for i, file in enumerate(FILES):
            logging.info("processing file {} / {} : {}".format(i, len(FILES), file))
            # try:
            records, document = process_file(file)
            with tag('Order'):
                with tag('OrderName'):
                    s = get(records, "translation order number", "übersetzungsauftragsnummer")[0]
                    try:
	                    s2 = get(records, "Brand and location", 'Brand and location')
	                    s = "{} - {}".format(s2[0], s)
                    except:
                    	pass

                    text(s)
                with tag("Customer"):
                    text(customer)
                with tag("Marke"):
                    text(MARKE)
                with tag("CatNum"):
                    text("")
                with tag("Type"):
                    text(TYPE)
                with tag("Section"):
                    text("")
                with tag("DateIn"):
                    text(DATE)
                with tag("Termin"):
                    s = get(records, "delivery date", "lieferdatum")
                    s = s[0].split(" ")[0].split("-")
                    s = ".".join([s[2].zfill(2), s[1].zfill(2), s[0]])
                    text(s)
                with tag("NameGe"):
                    text("")
                with tag("Name"):
                    s = get(records, "document title", "dokumententitel")
                    text(s[0])
                with tag("AnalyseScheme"):
                    text(SCHEME)
                with tag("ANXTranslated"):
                    s = get(records, "perfect match (words that match perfectly)", "perfect match (wörter, die mit perfectmatch übereinstimmen)")
                    tmp1 = int(s[0])
                    s = get(records, "context (words that match in context)", "context (wörter, die im kontext übereinstimmen)")
                    tmp2 = int(s[0])
                    text(str(tmp1 + tmp2))
                with tag("ANXTranslated"):
                    s = get(records, 'file repetitions (words that match across files)', 'file repetitions (wörter, die dateiübergreifend übereinstimmen)')
                    tmp1 = int(s[0])
                    s = get(records, 'repetitions', 'repetitions (wortwiederholungen)')
                    tmp2 = int(s[0])
                    text(str(tmp1 + tmp2))
                with tag("AN100"):
                    s = get(records, "100% match (excluding multiple 100% matches)", "100 prozent übereinstimmungen (exklusive mehrfache 100% matches)")
                    text(s[0])
                with tag("AN99_95"):
                    s = get(records, "95 - 99% match (including multiple 100 % matches)", "95 - 99 Prozent übereinstimmungen (inklusive mehrfache 100% matches)")
                    text(s[0])
                with tag("AN94_85"):
                    text("0")
                with tag("AN84_75"):
                    s = get(records, "75 - 94% match", "75 - 94 prozent übereinstimmungen")
                    text(s[0])
                with tag("AN74_50"):
                    text("0")
                with tag("AN0"):
                    s = get(records, "0 - 74% match", "0 - 74 prozent übereinstimmungen")
                    text(s[0])
                with tag("ANTotal"):
                    s = get(records, "total number of words", "gesamtzahl der wörter")
                    text(s[0])
                with tag("FT"):
                    text(FT)
                with tag("AnalyseLog"):
                    text(document)
            # except Exception as e:
            #     error("Exception {} while processing file {}. A bad csv file or VW changed the formatting.".format(e, file))

    result = yattag.indent(
        doc.getvalue(),
        indentation=' ' * 4,
        newline='\r\n'
    )

    result = header + "\n" + result
    with open(output, "w", encoding='latin-1') as f:
        f.write(result)
    info("Saved to file {}".format(output))

def process_file(file):
    records = []
    if not os.path.isfile(file):
        error("Could open {}. File does not exist.".format(file))
    # try:
    if True:
        logging.info("processing {}".format(file))
        with open(file, "r", encoding='latin-1') as f:
            text = f.read()
        with open(file, "r", encoding='latin-1') as f:
            reader = csv.DictReader(f, delimiter=";")
            [records.append(e) for e in reader]
    # except:
    else:
        error("Could not open file {}.".format(file))
        return False
    records1 = {e['c1'].lower() : [e['c2'], e['c3'], e['c4']] for e in records}
    return records1, text


if __name__ == "__main__":
    init_logging()
    file = "/home/emania/Documents/parser/data/1208205.csv"
    entry = {"files" : ["/home/emania/Documents/parser/data/1208205.csv", "/home/emania/Documents/parser/data/41081.csv"]}
    entry['ft'] = "468"
    entry['type'] = "RLF"
    entry['marke'] = "Audi"
    entry['scheme'] = "Audi 08"
    entry["output"] = "output.dar"
    entry['date'] = "25.11.2018"
    main(entry)

