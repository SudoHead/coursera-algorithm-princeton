import sys, os

if len(sys.argv) != 2:
    print("Usage: ./create_sub.py <submission_name>")
    sys.exit(1)

import glob, re, shutil

submission_name = sys.argv[1]

files = glob.glob("*.java")

if not os.path.exists(submission_name + '_copy'):
    os.mkdir(submission_name + '_copy')

original_txt = {}
for f in files:
    shutil.copyfile(f, os.path.join(submission_name + '_copy', os.path.basename(f)))
    with open(f, "r") as fp:
        og_code = fp.readlines()
        original_txt[f] = og_code
        # remove package line
        mod_code = [line for line in og_code if not line.startswith("package")]
        txt = "".join(mod_code)
        txt = re.sub(r'\n\s*\n', '\n', txt)
    with open(f, "w") as fp:
        fp.write(txt)

# create submission zip
import zipfile

zf = zipfile.ZipFile(submission_name, 'w')
for f in files:
    print("Adding file: " + f)
    zf.write(f)
zf.close()

# restore original files
og_files = glob.glob(f"{submission_name}_copy/*.java")

for old, new in zip(og_files, files):
    shutil.copyfile(old, new)

shutil.rmtree(submission_name + '_copy')
