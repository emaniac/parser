import os
import csv
from gui import warning, error, info, init_logging
import logging
import yattag

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
                    text(records["translation order number"][0])
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
                    text(records["delivery date"][0])
                with tag("NameGe"):
                    text("")
                with tag("Name"):
                    text(records["document title"][0])
                with tag("AnalyseScheme"):
                    text(SCHEME)
                with tag("ANXTranslated"):
                    tmp1 = int(records['perfect match (words that match perfectly)'][0])
                    tmp2 = int(records['context (words that match in context)'][0])
                    text(str(tmp1 + tmp2))
                with tag("ANXTranslated"):
                    tmp1 = int(records['file repetitions (words that match across files)'][0])
                    tmp2 = int(records['repetitions'][0])
                    text(str(tmp1 + tmp2))
                with tag("AN100"):
                    text(records["100% match (excluding multiple 100% matches)"][0])
                with tag("AN99_95"):
                    text(records["95 - 99% match (including multiple 100 % matches)"][0])
                with tag("AN94_85"):
                    text("0")
                with tag("AN84_75"):
                    text(records["75 - 94% match"][0])
                with tag("AN74_50"):
                    text("0")
                with tag("AN0"):
                    text(records["0 - 74% match"][0])
                with tag("ANTotal"):
                    text(records['total number of words'][0])
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

