## How to install

### On linux

```
apt-get install python3-dev
apt-get install python3-pip

git clone git@github.com:emaniac/parser.git -b dev
cd parser/python
pip3 install -r requirements.txt
```

### On windows

- Install git from [here](https://git-scm.com/downloads)
- Install python3 from [here](https://www.python.org/downloads/)
- Run the command line (win + R, cmd)
	- `pip3 install pyyaml`
	- `pip3 install yattag`
	- install other packages from requirements.txt


## How to launch

### On linux

- `cd` to the cloned directory 
- `python3 gui.py`

### On Windows

- Double click on the `gui.py`
- If not working, `cd` to the directory in command line and run `python3 gui.py` 
- Can create a shortcut using alt.

## How to update


### On Linux

- `cd` to the cloned directory
- `git checkout dev`
- `git pull`

### On Windows

- Use the file for update.
- If not working, open the file in text editor and check that the path is correct.