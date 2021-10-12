from COMP3003.Assignment02 import TextModification

# Start-up logic
print('Now starting emoji.py script...')
# Definition of TextModification Listener
class TextModification(TextModification):
    def modifyText(self, newValue):
        if ':-)' in newValue:
            return newValue.replace(':-)',u'\U0001f604')
        else:
            return newValue

# Make object of the class
listener = TextModification()
# Register listener with the API
api.registerScriptName("Emoji Script")
api.registerTextModification(listener)