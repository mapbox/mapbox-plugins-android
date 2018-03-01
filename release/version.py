#
# Read and update properties files
#

import click

def get_current_version(file_path, file_var):
    click.echo('Getting current version from %s.' % file_path)
    with open(file_path, 'r') as f:
        for line in f:
            if line.startswith(file_var):
                version_name = line[len(file_var):].strip()
                click.echo('Current version is %s.' % version_name)
                return version_name
    return None

def update_current_version(file_path, file_var, version):
    dirty = False
    click.echo('Updating file to version %s: %s.' % (version, file_path))
    with open(file_path, 'r') as f:
        file_lines = f.readlines()
    for line_number in range(len(file_lines)):
        if file_lines[line_number].startswith(file_var):
            content_old = file_lines[line_number]
            content_new = '%s%s\n' % (file_var, version)
            if content_old != content_new:
                click.echo('%s -> %s' % (content_old.strip(), content_new.strip()))
                file_lines[line_number] = content_new
                dirty = True
    if dirty:
        with open(file_path, 'w') as f:
            f.writelines(file_lines)
    else:
        click.echo('File already has the right version.')
    return dirty