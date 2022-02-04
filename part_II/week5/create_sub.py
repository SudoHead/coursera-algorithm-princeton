import sys, os, argparse

parser = argparse.ArgumentParser(description='Creates a submission ready zip file with the .java files in the current directory.')
parser.add_argument('output', help='The name of the output zip file.')
# add argparse argument to exclude files
parser.add_argument('-e', '--exclude', nargs='+', help='The files to exclude from the zip file.')

args = parser.parse_args()

print('Creating submission ' + args.output)

import glob, re, shutil

submission_name = args.output

files = glob.glob("*.java")

temp_dir = submission_name + '_copy'
if not os.path.exists(temp_dir):
    os.mkdir(temp_dir)

original_txt = {}
sub_files = []
for f in files:
    if args.exclude is not None:
        if f in args.exclude:
            continue
    copied_file = os.path.join(temp_dir, os.path.basename(f))
    shutil.copyfile(f, copied_file)
    with open(copied_file, "r") as fp:
        og_code = fp.readlines()
        original_txt[f] = og_code
        # remove package line
        mod_code = [line for line in og_code if not line.startswith("package")]
        txt = "".join(mod_code)
        txt = re.sub(r'\n\s*\n', '\n', txt)
    with open(copied_file, "w") as fp:
        fp.write(txt)
    sub_files.append(copied_file)

# create submission zip
import zipfile

zf = zipfile.ZipFile(submission_name, 'w')
for f in sub_files:
    print("Adding file: " + f)
    zf.write(f)
zf.close()

shutil.rmtree(temp_dir)
