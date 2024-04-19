import csv

def delete_empty_columns(input_file, output_file):
    with open(input_file, "r", encoding='utf-8', newline='\n') as input_file:
        csv_reader = csv.reader(input_file)
        rows = list(csv_reader)
        
        # Identify empty columns
        empty_colums = set()
        for row in rows: 
            for i, value in enumerate(row):
                if not value.strip():
                    empty_colums.add(i)

        # Write non_empty columns to the output CSV file 
        with open(output_file, "w", newline='') as output_file:
            csv_writer = csv.writer(output_file)
            for row in rows:
                new_row = [value for i, value in enumerate(row) if i not in empty_colums]
                csv_writer.writerow(new_row)







